package com.juane.remotecontrol.ui.fragments;

import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.juane.remotecontrol.network.ClientManager;
import com.juane.remotecontrol.utils.Utils;

/**
 * Fragment with ClientManager to call server from any Fragment that extends this class
 */

class RemoteFragment extends Fragment {
    static ClientManager clientManager;
    TextView versionText;

    public RemoteFragment() {
        if (clientManager == null) {
            clientManager = new ClientManager();
        }
    }

    void setVersionTextView() {
        versionText.setText("By juanE " + Utils.getVersion(this));
    }

}
