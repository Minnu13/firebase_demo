package com.example.demoproject.tabs;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.demoproject.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyViewPagerAdapter adapter;
    private ArrayList<String> permissionsList;
    private ActivityResultLauncher<String[]> permissionsLauncher;
    private String[] permissionsStr = {
            Manifest.permission.CAMERA,
            ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private int permissionsCount = 0;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tabs_activity);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);

        ArrayList<String> names = new ArrayList<>();
        names.add("Tab 1");
        names.add("Tab 2");
        names.add("Tab 3");

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new FragmentA());
        fragments.add(new FragmentB());
        fragments.add(new FragmentC());

        adapter = new MyViewPagerAdapter(getSupportFragmentManager(), names, fragments);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        permissionsList = new ArrayList<>();
        permissionsList.addAll(Arrays.asList(permissionsStr));

        // Callback Listener for permission
        permissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                ArrayList<Boolean> list = new ArrayList<>(result.values());
                permissionsList = new ArrayList<>();
                permissionsCount = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (shouldShowRequestPermissionRationale(permissionsStr[i])) {
                        permissionsList.add(permissionsStr[i]);
                    } else if (!hasPermission(MainActivity.this, permissionsStr[i])) {
                        permissionsCount++;
                    }
                }
                if (permissionsList.size() > 0) {
                    //Some permissions are denied and can be asked again.
                    askForPermissions();
                } else if (permissionsCount > 0) {
                    //Show alert dialog
                    showPermissionDialog();
                } else {
                    //All permissions granted. Do your stuff 🤞
                    Toast.makeText(MainActivity.this, "All Permission has granted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        askForPermissions(); // ask for permission invoke

    }

    // check if has permission
    private boolean hasPermission(Context context, String permissionStr) {
        return ContextCompat.checkSelfPermission(context, permissionStr) == PackageManager.PERMISSION_GRANTED;
    }

    // ask for permissions
    private void askForPermissions() {
        String[] newPermissionStr = new String[permissionsList.size()];
        for (int i = 0; i < newPermissionStr.length; i++) {
            newPermissionStr[i] = permissionsList.get(i);
        }
        if (newPermissionStr.length > 0) {
            permissionsLauncher.launch(newPermissionStr);
        } else {
        /* User has pressed 'Deny & Don't ask again' so we have to show the enable permissions dialog
        which will lead them to app details page to enable permissions from there. */
            //showPermissionDialog();
        }
    }

    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Permission required")
                .setMessage("Some permissions are need to be allowed to use this app without any problems.")
                .setPositiveButton("OK", (dialog, which) -> {
                 /*   Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);*/
                    dialog.dismiss();
                });
        if (alertDialog == null) {
            alertDialog = builder.create();
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
    }
}
