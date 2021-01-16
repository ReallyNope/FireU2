package net.myapp.Organize4U;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.opentok.android.OpentokError;
import com.opentok.android.PublisherKit;
import com.opentok.android.Publisher;
import com.opentok.android.Session;
import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit. PublisherListener{


    private  static String API_Key = "47000064";
    private String SESSION_ID = "1_MX40NzAwMDA2NH5-MTYwNjE0NDExNDI2Nn5FSi9OWUpwN3pJTENURU16UkpUTmVBZ0F-fg";
    private String TOKEN = "T1==cGFydG5lcl9pZD00NzAwMDA2NCZzaWc9MjFmMWE2NWNkM2EwNDA1Y2FiNWQ0ODNkOTg4YjUwMmJjZDlmMmRlYzpzZXNzaW9uX2lkPTFfTVg0ME56QXdNREEyTkg1LU1UWXdOakUwTkRFeE5ESTJObjVGU2k5T1dVcHdOM3BKVEVOVVJVMTZVa3BVVG1WQlowRi1mZyZjcmVhdGVfdGltZT0xNjA2MTQ0MjA3Jm5vbmNlPTAuMzQ4NzMxMzkxODcwMzMxJnJvbGU9cHVibGlzaGVyJmV4cGlyZV90aW1lPTE2MDY3NDkwMDgmaW5pdGlhbF9sYXlvdXRfY2xhc3NfbGlzdD0=";
    private Subscriber mSubscriber;
    private static final String LOG_TAG = VideoActivity.class.getSimpleName();
    private static final int RC_VIDEO_APP_PERM =  124;
    private ImageView closeVideoChatBtn;
    private DatabaseReference userRef;
    private String userID="";
    private Publisher mPublisher;
    private Session mSession;
    private FrameLayout mPublisherViewController;
    private FrameLayout mSubscriberViewController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userID = FirebaseAuth.getInstance().getUid();


        closeVideoChatBtn = findViewById(R.id.close_video_chat_btn);
        closeVideoChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(userID).hasChild("Ringing")) {
                            userRef.child(userID).child("Ringing").removeValue();

                            if(mPublisher != null)
                            {
                                mPublisher.destroy();
                            }
                            if(mSubscriber != null)
                            {
                                mSubscriber.destroy();
                            }
                            startActivity((new Intent(VideoActivity.this, MainActivity.class)));
                            finish();
                        }
                        if (dataSnapshot.child(userID).hasChild("Calling")) {
                            userRef.child(userID).child("Calling").removeValue();

                            if(mPublisher != null)
                            {
                                mPublisher.destroy();
                            }
                            if(mSubscriber != null)
                            {
                                mSubscriber.destroy();
                            }

                            startActivity((new Intent(VideoActivity.this, MainActivity.class)));
                            finish();
                        }
                        else
                        {
                            startActivity((new Intent(VideoActivity.this, MainActivity.class)));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        requestpermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, VideoActivity.this);
    }
    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestpermissions()
    {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};
        if(EasyPermissions.hasPermissions(this, perms))
        {
            mPublisherViewController = findViewById(R.id.publisher_container);
            mSubscriberViewController = findViewById(R.id.subscriber_container);

            mSession = new Session.Builder(this,API_Key,SESSION_ID).build();
            mSession.setSessionListener(VideoActivity.this);
            mSession.connect(TOKEN);
        }
        else
            {
            EasyPermissions.requestPermissions(this, "app need microphone and camera permission to start video chat", RC_VIDEO_APP_PERM, perms);
            }
    }


        @Override
        public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

        }

        @Override
        public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

        }

        @Override
        public void onError(PublisherKit publisherKit, OpentokError opentokError) {

        }

        @Override
        public void onConnected(Session session) {
            Log.i(LOG_TAG, "Session Conected");

             mPublisher = new Publisher.Builder(this).build();
             mPublisher.setPublisherListener(VideoActivity.this);

             mPublisherViewController.addView(mPublisher.getView());

            if(mPublisher.getView() instanceof GLSurfaceView)
            {
                ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
            }

            mSession.publish(mPublisher);
        }

        @Override
        public void onDisconnected(Session session) {
            Log.i(LOG_TAG, "Stream Disconnected");
        }

        @Override
        public void onStreamReceived(Session session, Stream stream)
        {
            Log.i(LOG_TAG, "Stream Received");

            if(mSubscriber == null)
            {
                mSubscriber = new Subscriber.Builder(this, stream).build();
                mSession.subscribe(mSubscriber);
                mSubscriberViewController.addView(mSubscriber.getView());
            }
        }

        @Override
        public void onStreamDropped(Session session, Stream stream)
        {
            Log.i(LOG_TAG, "Stream Dropped");

            if(mSubscriber != null)
            {
                mSubscriber = null;
                mPublisherViewController.removeAllViews();
            }
        }

        @Override
        public void onError(Session session, OpentokError opentokError)
        {
            Log.i(LOG_TAG, "Stream Error");
        }

        @Override
        public void onPointerCaptureChanged(boolean hasCapture) {

        }

}

