package km.gxy.com.funtest;

import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * @author xiayi.gu@2020/8/8
 */
public class BaseFragment extends Fragment {

    public void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int msgId) {
        Toast.makeText(getContext(), msgId, Toast.LENGTH_SHORT).show();
    }
}
