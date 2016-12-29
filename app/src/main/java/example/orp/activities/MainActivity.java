package example.orp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.aistech.orp.activities.ORPActivity;
import com.github.aistech.orp.builder.ORPBuilder;

import org.parceler.Parcels;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

import example.orp.R;
import example.orp.enums.PassingType;
import example.orp.model.User;
import example.orp.util.CountdownUtils;

public class MainActivity extends ORPActivity {

    public static final PassingType PASSING_TYPE = PassingType.PARCEL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.activity_main);

        (findViewById(R.id.fab_parcel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Glide.with(MainActivity.this)
                        .load("http://i.imgur.com/TDROo.jpg")
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                Bitmap bitmap = ((GlideBitmapDrawable) resource.getCurrent()).getBitmap();
                                LinkedList<User> users = new LinkedList<User>();

                                for (int x = 0; x < 10; x++) {
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                    users.add(new User("Master Chief", 117, new ByteArrayInputStream(stream.toByteArray()).toString()));
                                }

                                Log.w("ORP: ", "PARCEL");
                                CountdownUtils.getInstance().start();

                                Intent intent = new Intent(MainActivity.this, ParcelCaseActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("users", Parcels.wrap(users));
                                intent.putExtras(bundle);
                                MainActivity.this.startActivity(intent);
                            }
                        });
            }
        });

        (findViewById(R.id.fab_serialization)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Glide.with(MainActivity.this)
                        .load("http://i.imgur.com/TDROo.jpg")
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                Bitmap bitmap = ((GlideBitmapDrawable) resource.getCurrent()).getBitmap();
                                LinkedList<User> users = new LinkedList<User>();

                                for (int x = 0; x < 10; x++) {
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                    users.add(new User("Master Chief", 117, new ByteArrayInputStream(stream.toByteArray()).toString()));
                                }

                                Log.w("ORP: ", "SERIAL");
                                CountdownUtils.getInstance().start();

                                Intent intent = new Intent(MainActivity.this, SerializationCaseActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("users", (Serializable) users);
                                intent.putExtras(bundle);
                                MainActivity.this.startActivity(intent);
                            }
                        });
            }
        });

        (findViewById(R.id.fab_orp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Glide.with(MainActivity.this)
                        .load("http://i.imgur.com/TDROo.jpg")
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                Bitmap bitmap = ((GlideBitmapDrawable) resource.getCurrent()).getBitmap();
                                LinkedList<User> users = new LinkedList<User>();

                                for (int x = 0; x < 10; x++) {
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                    users.add(new User("Master Chief", 117, new ByteArrayInputStream(stream.toByteArray()).toString()));
                                }

                                Log.w("ORP: ", "ORP");
                                CountdownUtils.getInstance().start();

                                new ORPBuilder(MainActivity.this)
                                        .withDestinationActivity(ORPCaseActivity.class)
                                        .passingObject("users", users)
                                        .start();
                            }
                        });
            }
        });
    }
}
