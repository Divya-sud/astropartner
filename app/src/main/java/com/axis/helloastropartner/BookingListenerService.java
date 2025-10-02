package com.axis.helloastropartner;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

public class BookingListenerService extends Service {

    private ListenerRegistration listener;
    private MediaPlayer mediaPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String astrologerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        listener = db.collection("bookings")
                .whereEqualTo("astrologerId", astrologerId)
                .whereEqualTo("status", "pending")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null) return;

                    for (DocumentChange change : snapshots.getDocumentChanges()) {
                        if (change.getType() == DocumentChange.Type.ADDED) {
                            Booking booking = change.getDocument().toObject(Booking.class);
                            handleIncomingBooking(booking);
                        }
                    }
                });

        return START_STICKY;
    }

    private void handleIncomingBooking(Booking booking) {
        playRingtone();

        Intent intent;
        switch (booking.getMode()) {
            case "chat":
                intent = new Intent(this, ChatActivity.class);
                break;
            case "video":
                intent = new Intent(this, VideoCallActivity.class);
                break;
            case "call":
                intent = new Intent(this, VoiceCallActivity.class);
                break;
            default:
                return;
        }

        intent.putExtra("bookingId", booking.getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void playRingtone() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, android.provider.Settings.System.DEFAULT_RINGTONE_URI);
            mediaPlayer.setLooping(true);
        }
        mediaPlayer.start();

        // Stop after 10 seconds
        new android.os.Handler().postDelayed(() -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }, 10000);
    }

    @Override
    public void onDestroy() {
        if (listener != null) listener.remove();
        if (mediaPlayer != null) mediaPlayer.release();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

