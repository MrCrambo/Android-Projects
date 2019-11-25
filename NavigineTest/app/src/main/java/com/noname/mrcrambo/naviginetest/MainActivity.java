package com.noname.mrcrambo.naviginetest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Bundle;
import com.navigine.naviginesdk.DeviceInfo;
import com.navigine.naviginesdk.Location;
import com.navigine.naviginesdk.LocationPoint;
import com.navigine.naviginesdk.LocationView;
import com.navigine.naviginesdk.NavigationThread;
import com.navigine.naviginesdk.NavigineSDK;
import com.navigine.naviginesdk.RoutePath;
import com.navigine.naviginesdk.SubLocation;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final int           LOCATION_ID               = 41366;
    private int                 mCurrentSubLocationIndex  = 0;

    private LocationView        mLocationView             = null;
    private Location            mLocation                 = null;
    private NavigationThread    mNavigation               = null;
    private DeviceInfo          mDeviceInfo               = null;
    private float               mDisplayDensity           = 0.0f;
    private long                mAdjustTime               = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationView = findViewById(R.id.navigine_location_view);
        mLocationView.setBackgroundColor(0xffebebeb);
        mLocationView.setListener(new LocationView.Listener(){

            @Override
            public void onLongClick(float x, float y) {
                makePin(mLocationView.getAbsCoordinates(x, y));
            }

            @Override
            public void onDraw(Canvas canvas) {
                drawDevice(canvas);
            }
        });

        mLocationView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            int width  = right  - left;
            int height = bottom - top;
            if (width == 0 || height == 0)
                return;
            int oldWidth  = oldRight  - oldLeft;
            int oldHeight = oldBottom - oldTop;
            if (oldWidth != width || oldHeight != height) {
                loadMap();
            }
        });

        mDisplayDensity = getResources().getDisplayMetrics().density;

        mNavigation = NavigineSDK.getNavigation();
        if (mNavigation != null) {
            mNavigation.setDeviceListener(new DeviceInfo.Listener() {
                @Override public void onUpdate(DeviceInfo info) { handleDeviceUpdate(info); }
            });
        } else {
            System.out.println("smth wents wrong!");
        }
    }

    private void makePin(PointF P) {
        if (mLocation == null || mCurrentSubLocationIndex < 0)
            return;

        SubLocation subLoc = mLocation.subLocations.get(mCurrentSubLocationIndex);
        if (subLoc == null)
            return;

        if (P.x < 0.0f || P.x > subLoc.width ||
                P.y < 0.0f || P.y > subLoc.height)
        {
            // Missing the map
            return;
        }


        if (mDeviceInfo == null || !mDeviceInfo.isValid())
            return;

        LocationPoint mPinPoint = new LocationPoint(mLocation.id, subLoc.id, P.x, P.y);
        mNavigation.setTarget(mPinPoint);
        mLocationView.redraw();
    }

    private void handleDeviceUpdate(DeviceInfo deviceInfo) {
        mDeviceInfo = deviceInfo;
        if (deviceInfo == null)
            return;

        // Check if location is loaded
        if (mLocation == null || mCurrentSubLocationIndex < 0)
            return;

        if (deviceInfo.isValid()) {
            //adjustDevice();
        }
        else {
            switch (deviceInfo.errorCode) {
                case 4:
                    System.out.println("You are out of navigation zone! Please, check that your bluetooth is enabled!");
                    break;

                case 8:
                case 30:
                    System.out.println("Not enough beacons on the location! Please, add more beacons!");
                    break;

                default:
                    System.out.println(String.format(Locale.ENGLISH,
                            "Something is wrong with location '%s' (error code %d)! " +
                                    "Please, contact technical support!",
                            mLocation.name, deviceInfo.errorCode));
                    break;
            }
        }

        // This causes map redrawing
        mLocationView.redraw();
    }

    private void adjustDevice()
    {
        // Check if location is loaded
        if (mLocation == null || mCurrentSubLocationIndex < 0)
            return;

        // Check if navigation is available
        if (mDeviceInfo == null || !mDeviceInfo.isValid())
            return;

        long timeNow = System.currentTimeMillis();

        // Adjust map, if necessary
        if (timeNow >= mAdjustTime)
        {
            // Firstly, set the correct sublocation
            SubLocation subLoc = mLocation.subLocations.get(mCurrentSubLocationIndex);
            if (mDeviceInfo.subLocation != subLoc.id)
            {
                for(int i = 0; i < mLocation.subLocations.size(); ++i)
                    if (mLocation.subLocations.get(i).id == mDeviceInfo.subLocation)
                        loadSubLocation(i);
            }

            // Secondly, adjust device to the center of the screen
            PointF center = mLocationView.getScreenCoordinates(mDeviceInfo.x, mDeviceInfo.y);
            float deltaX  = mLocationView.getWidth()  / 2 - center.x;
            float deltaY  = mLocationView.getHeight() / 2 - center.y;
            mAdjustTime   = timeNow;
            mLocationView.scrollBy(deltaX, deltaY);
        }
    }

    private void drawDevice(Canvas canvas) {
        // Check if location is loaded
        if (mLocation == null || mCurrentSubLocationIndex < 0)
            return;

        // Check if navigation is available
        if (mDeviceInfo == null || !mDeviceInfo.isValid())
            return;

        // Get current sublocation displayed
        SubLocation subLoc = mLocation.subLocations.get(mCurrentSubLocationIndex);

        if (subLoc == null)
            return;

        final int solidColor  = Color.argb(255, 64,  163, 205); // Light-blue color
        final int circleColor = Color.argb(127, 64,  163, 205); // Semi-transparent light-blue color
        final int arrowColor  = Color.argb(255, 255, 255, 255); // White color
        final float dp = mDisplayDensity;

        // Preparing paints
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        /// Drawing device path (if it exists)
        if (mDeviceInfo.paths != null && mDeviceInfo.paths.size() > 0) {
            RoutePath path = mDeviceInfo.paths.get(0);
            if (path.points.size() >= 2) {
                paint.setColor(solidColor);

                for(int j = 1; j < path.points.size(); ++j) {
                    LocationPoint P = path.points.get(j-1);
                    LocationPoint Q = path.points.get(j);
                    if (P.subLocation == subLoc.id && Q.subLocation == subLoc.id) {
                        paint.setStrokeWidth(3 * dp);
                        PointF P1 = mLocationView.getScreenCoordinates(P);
                        PointF Q1 = mLocationView.getScreenCoordinates(Q);
                        canvas.drawLine(P1.x, P1.y, Q1.x, Q1.y, paint);
                    }
                }
            }
        }

        paint.setStrokeCap(Paint.Cap.BUTT);

        // Check if device belongs to the current sublocation
        if (mDeviceInfo.subLocation != subLoc.id)
            return;

        final float x  = mDeviceInfo.x;
        final float y  = mDeviceInfo.y;
        final float r  = mDeviceInfo.r;
        final float angle = mDeviceInfo.azimuth;
        final float sinA = (float)Math.sin(angle);
        final float cosA = (float)Math.cos(angle);
        final float radius  = mLocationView.getScreenLengthX(r);  // External radius: navigation-determined, transparent
        final float radius1 = 25 * dp;                            // Internal radius: fixed, solid

        PointF O = mLocationView.getScreenCoordinates(x, y);
        PointF P = new PointF(O.x - radius1 * sinA * 0.22f, O.y + radius1 * cosA * 0.22f);
        PointF Q = new PointF(O.x + radius1 * sinA * 0.55f, O.y - radius1 * cosA * 0.55f);
        PointF R = new PointF(O.x + radius1 * cosA * 0.44f - radius1 * sinA * 0.55f, O.y + radius1 * sinA * 0.44f + radius1 * cosA * 0.55f);
        PointF S = new PointF(O.x - radius1 * cosA * 0.44f - radius1 * sinA * 0.55f, O.y - radius1 * sinA * 0.44f + radius1 * cosA * 0.55f);

        // Drawing transparent circle
        paint.setStrokeWidth(0);
        paint.setColor(circleColor);
        canvas.drawCircle(O.x, O.y, radius, paint);

        // Drawing solid circle
        paint.setColor(solidColor);
        canvas.drawCircle(O.x, O.y, radius1, paint);

        // Drawing arrow
        paint.setColor(arrowColor);
        Path path = new Path();
        path.moveTo(Q.x, Q.y);
        path.lineTo(R.x, R.y);
        path.lineTo(P.x, P.y);
        path.lineTo(S.x, S.y);
        path.lineTo(Q.x, Q.y);
        canvas.drawPath(path, paint);
    }

    private boolean loadMap() {

        mNavigation.cancelTargets();
        if (!mNavigation.loadLocation(LOCATION_ID)) {
            return false; // Location is not loaded
        }

        mLocation = mNavigation.getLocation();
        return loadSubLocation(mCurrentSubLocationIndex);
    }

    private boolean loadSubLocation(int index) {
        if (mLocation == null) {
            return false; // Location is not loaded
        }

        System.out.println(mLocation.subLocations.size());
        if (index < 0 || index >= mLocation.subLocations.size()) {
            return false; // Invalid sub location index
        }

        SubLocation subLoc = mLocation.subLocations.get(index);
        if (subLoc.width < 1.0f || subLoc.height < 1.0f) {
            return false; // Invalid sub location size

        }

        if (!mLocationView.loadSubLocation(subLoc)) {
            return false; // Error loading sub location
        }

        // Initializing zoom factor to fit the view (if possible)
        float viewWidth  = mLocationView.getWidth();
        float viewHeight = mLocationView.getHeight();
        float minZoomFactor = Math.min(viewWidth/subLoc.width, viewHeight/subLoc.height);
        float maxZoomFactor = LocationView.ZOOM_FACTOR_MAX;
        mLocationView.setZoomRange(minZoomFactor, maxZoomFactor);
        mLocationView.setZoomFactor(minZoomFactor);

        mNavigation.setMode(NavigationThread.MODE_NORMAL);
        mLocationView.redraw();
        return true;
    }

}
