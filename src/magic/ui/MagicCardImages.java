package magic.ui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Proxy;
import magic.cardBuilder.renderers.CardBuilder;
import magic.data.CardImageFile;
import magic.data.GeneralConfig;
import magic.model.IRenderableCard;
import magic.ui.screen.images.download.CardImageDisplayMode;
import magic.utility.MagicFileSystem;

/**
 * Handles card images only via IRenderableCard.
 */
public final class MagicCardImages {

    private enum ImageType {
        UNKNOWN,
        CUSTOM,
        PROXY,
        PRINTED
    }

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();
    private static Proxy proxy;

    private static ImageType getImageType(IRenderableCard face) {

        if (face.isUnknown()) {
            return ImageType.UNKNOWN;
        }

        if (MagicFileSystem.getCustomCardImageFile(face).exists()) {
            return ImageType.CUSTOM;
        }

        if (CONFIG.getCardImageDisplayMode() == CardImageDisplayMode.PROXY) {
            return ImageType.PROXY;
        }

        if (MagicFileSystem.getPrintedCardImage(face).exists() || CONFIG.getImagesOnDemand()) {
            return ImageType.PRINTED;
        }

        // else missing image proxy...
        return ImageType.PROXY;
    }

    private static void tryDownloadingPrintedImage(IRenderableCard face) {
        if (proxy == null) {
            proxy = CONFIG.getProxy();
        }
        try {
            CardImageFile cif = new CardImageFile(face);
            cif.doDownload(proxy);
        } catch (IOException ex) {
            System.err.println(face.getCardDefinition().getDistinctName() + " : " + ex);
        }
    }

    public static BufferedImage createImage(IRenderableCard face) {
        final ImageType type = getImageType(face);
        switch (type) {
            case UNKNOWN:
                return MagicImages.MISSING_CARD;
            case CUSTOM:
                return ImageFileIO.getOptimizedImage(MagicFileSystem.getCustomCardImageFile(face));
            case PROXY:
                return CardBuilder.getCardBuilderImage(face);
            case PRINTED:
                if (!MagicFileSystem.getPrintedCardImage(face).exists()) {
                    // on-demand image download.
                    tryDownloadingPrintedImage(face);
                }
                if (MagicFileSystem.getPrintedCardImage(face).exists()) {
                    if (CONFIG.getCardImageDisplayMode() == CardImageDisplayMode.PRINTED || CONFIG.getCardTextLanguage() != CardTextLanguage.ENGLISH) {
                        return ImageFileIO.getOptimizedImage(MagicFileSystem.getPrintedCardImage(face));
                    } else {
                        return face.isPlaneswalker() || face.isFlipCard() || face.isToken()
                            ? ImageFileIO.getOptimizedImage(MagicFileSystem.getPrintedCardImage(face))
                            : CardBuilder.getCardBuilderImage(face);
                    }
                }
        }
        return CardBuilder.getCardBuilderImage(face);
    }

    public static boolean isProxyImage(IRenderableCard face) {
        return getImageType(face) == ImageType.PROXY;
    }

    private MagicCardImages() {}
}
