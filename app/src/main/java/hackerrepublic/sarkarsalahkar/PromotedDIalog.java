package hackerrepublic.sarkarsalahkar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by yash on 20/9/17.
 */

public class PromotedDIalog extends Dialog {

    Context context;

    public PromotedDIalog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_promoted_dialog);
    }
}
