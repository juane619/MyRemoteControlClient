package com.juane.remotecontrol.ui.fragments;

import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.juane.remotecontrol.network.ClientManager;
import com.juane.remotecontrol.utils.Utils;

/**
 * Fragment with ClientManager to call server from any Fragment that extends this class
 */

public class RemoteFragment extends Fragment {
    protected static ClientManager clientManager;

    protected TextView versionText;

    public RemoteFragment() {
        if (clientManager == null) {
            clientManager = new ClientManager();
        }
    }

    protected void setVersionTextView() {
        versionText.setText("By juanE " + Utils.getVersion(this));
    }

}
