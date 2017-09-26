package hackerrepublic.sarkarsalahkar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

/**
 * Creates the promoted dialog box that is shown to the user
 * when his level is promoted.
 *
 * @author shilpi75
 */
public class PromotedDIalog extends Dialog {

    Context context;

    /**
     * Constructor.
     * @param context
     */
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
