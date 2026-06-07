package com.rainmachine.presentation.screens.networksettings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.rainmachine.R;
import com.rainmachine.presentation.activities.DrawerActivity;

import butterknife.ButterKnife;

public class NetworkSettingsActivity extends DrawerActivity {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, NetworkSettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGraphAndInject();
        setContentView(R.layout.activity_network_settings);
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        linkToolbar(toolbar);

        drawerHelper.setupDrawer(toolbar);
    }

    public Object getModule() {
        return new NetworkSettingsModule(this);
    }
}
