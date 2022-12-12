package voterSearch.app.SmdInfo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by Kamlesh on 12/11/2016.
 */
public class Utility {
    public static Dialog mAlertPopUp = null;

    public static GradientDrawable getRectangleBorder(int solidColor, float[] radius, int strokeWidth, int strokeColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(solidColor);
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadii(radius);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        return gradientDrawable;
    }

    public static GradientDrawable getCircularBorder(int solidColor, int strokeColor, int strokeWidth) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(solidColor);
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        return gradientDrawable;
    }

    /**
     * @param context
     * @return true if device type is mobile otherwise false
     */
    public static boolean IsScreenTypeMobile(Context context) {
        String ua = new WebView(context).getSettings().getUserAgentString();
        return ua.contains("Mobile");
    }

    public static int getDeviceHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getDeviceWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static void setSelector(Context context, View v, int Stroke, int PrimarySolidColor,
                                   int PressedSolidColor, int PrimaryBorderColor, int PressedBOrderColor) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, getRectangleBorder(context.getResources()
                        .getColor(PressedSolidColor), new float[]{0, 0, 0, 0, 0, 0, 0, 0}, Stroke,
                context.getResources().getColor(PressedBOrderColor)));
        states.addState(new int[]{}, getRectangleBorder(context.getResources().getColor(
                PrimarySolidColor), new float[]{0, 0, 0, 0, 0, 0, 0, 0}, Stroke,
                context.getResources().getColor(PrimaryBorderColor)));
        v.setBackgroundDrawable(states);

    /*   if (v instanceof TextView){
            ((TextView) v).setTextColor(new ColorStateList(
                    new int[][]{new int[]{android.R.attr.state_pressed},
                            new int[0]
                    }, new int[]{
                    Color.rgb(255, 128, 192),
                    Color.WHITE,
            }
            ));
        }
        if (v instanceof Button) {
            ((Button) v).setTextColor(new ColorStateList(
                    new int[][]{new int[]{android.R.attr.state_pressed},
                            new int[0]
                    }, new int[]{
                    Color.rgb(255, 128, 192),
                    Color.WHITE,
            }
            ));
        }*/

    }

    public static void setSelectorRoundedCorner(Context context, View v, int Stroke, int PrimarySolidColor,
                                                int PressedSolidColor, int PrimaryBorderColor, int PressedBOrderColor,
                                                int radius) {
        StateListDrawable states = new StateListDrawable();

        states.addState(new int[]{android.R.attr.state_pressed}, getRectangleBorder(context.getResources()
                        .getColor(PressedSolidColor), new float[]{radius, radius, radius, radius, radius, radius, radius, radius}, Stroke,
                context.getResources().getColor(PressedBOrderColor)));
        states.addState(new int[]{}, getRectangleBorder(context.getResources().getColor(
                PrimarySolidColor), new float[]{radius, radius, radius, radius, radius, radius, radius, radius}, Stroke,
                context.getResources().getColor(PrimaryBorderColor)));
        v.setBackgroundDrawable(states);

    }



}
