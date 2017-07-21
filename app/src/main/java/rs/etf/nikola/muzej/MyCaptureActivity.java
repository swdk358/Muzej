package rs.etf.nikola.muzej;

import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Button;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import rs.etf.nikola.muzej.R;

public class MyCaptureActivity extends CaptureActivity {
    private DecoratedBarcodeView decoratedBarcodeView;
    private Button switchFlashlightButton;

    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.custom_scanner_layout);

        decoratedBarcodeView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);

        switchFlashlightButton = (Button) findViewById(R.id.switch_flashlight);
        if (!hasFlash()) {
            switchFlashlightButton.setVisibility(View.GONE);
        }

        decoratedBarcodeView.setTorchListener(new DecoratedBarcodeView.TorchListener() {
            @Override
            public void onTorchOn() {
                switchFlashlightButton.setText(R.string.turn_off_flashlight);
            }

            @Override
            public void onTorchOff() {
                switchFlashlightButton.setText(R.string.turn_on_flashlight);
            }
        });

        return decoratedBarcodeView;
    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight(View view) {
        if (getString(R.string.turn_on_flashlight).equals(switchFlashlightButton.getText())) {
            decoratedBarcodeView.setTorchOn();
        } else {
            decoratedBarcodeView.setTorchOff();
        }
    }
}
