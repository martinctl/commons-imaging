/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.imaging.palette;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.image.BufferedImage;

import org.apache.commons.imaging.AbstractImagingTest;
import org.apache.commons.imaging.ImagingException;
import org.junit.jupiter.api.Test;

public class PaletteQuantizationTest extends AbstractImagingTest {

    private void checkPaletteDetails(final BufferedImage image, final int limit, final int expectedSize) throws ImagingException {
        final PaletteFactory paletteFactory = new PaletteFactory();
        Palette palette;

        palette = paletteFactory.makeExactRgbPaletteSimple(image, limit);
        // too small palettes are null
        boolean exact = false;
        if (palette != null) {
            assertEquals(expectedSize, palette.length());
            // checkUniqueColors(image, palette);
            exact = true;
        }
        if (exact) {
            // checkDithering(image, palette);
        }

        palette = paletteFactory.makeQuantizedRgbaPalette(image, false, limit);
        assertEquals(expectedSize, palette.length());
        checkUniqueColors(image, palette);
        if (exact) {
            checkPixelsAreIdentical(image, palette);
        }

        palette = paletteFactory.makeQuantizedRgbPalette(image, limit);
        assertEquals(expectedSize, palette.length());
        // checkUniqueColors(image, palette);
        if (exact) {
            // checkDithering(image, palette);
        }

        final MedianCutQuantizer medianCutQuantizer = new MedianCutQuantizer(true);
        palette = medianCutQuantizer.process(image, limit, new MostPopulatedBoxesMedianCut());
        assertEquals(expectedSize, palette.length());
        checkUniqueColors(image, palette);
        if (exact) {
            checkPixelsAreIdentical(image, palette);
        }
    }

    private void checkPixelsAreIdentical(final BufferedImage src, final Palette palette) throws ImagingException {
        final BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        dst.getGraphics().drawImage(src, 0, 0, src.getWidth(), src.getHeight(), null);
        Dithering.applyFloydSteinbergDithering(dst, palette);
        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                assertEquals(src.getRGB(x, y), dst.getRGB(x, y));
            }
        }
    }

    private void checkUniqueColors(final BufferedImage src, final Palette palette) throws ImagingException {
        final BufferedImage dst = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        dst.getGraphics().drawImage(src, 0, 0, src.getWidth(), src.getHeight(), null);
        Dithering.applyFloydSteinbergDithering(dst, palette);
        final Palette ditheredPalette = new PaletteFactory().makeExactRgbPaletteSimple(dst, palette.length() * 2);
        assertEquals(palette.length(), ditheredPalette.length());
    }

    @Test
    public void testPaletteQuantization() throws ImagingException {
        final BufferedImage whiteImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < whiteImage.getHeight(); y++) {
            for (int x = 0; x < whiteImage.getWidth(); x++) {
                whiteImage.setRGB(x, y, 0xFFFFFF);
            }
        }
        checkPaletteDetails(whiteImage, 10, 1);

        final BufferedImage whiteAndBlackImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < whiteImage.getHeight(); y++) {
            for (int x = 0; x < 5; x++) {
                whiteAndBlackImage.setRGB(x, y, 0xFFFFFF);
            }
            for (int x = 5; x < 10; x++) {
                whiteAndBlackImage.setRGB(x, y, 0x000000);
            }
        }
        checkPaletteDetails(whiteAndBlackImage, 10, 2);

        final BufferedImage rainbowImage = new BufferedImage(9, 10, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < whiteImage.getHeight(); y++) {
            for (int x = 0; x < 3; x++) {
                rainbowImage.setRGB(x, y, 0xFF0000);
            }
            for (int x = 3; x < 6; x++) {
                rainbowImage.setRGB(x, y, 0x00FF00);
            }
            for (int x = 6; x < 9; x++) {
                rainbowImage.setRGB(x, y, 0x0000FF);
            }
        }
        checkPaletteDetails(rainbowImage, 10, 3);
        checkPaletteDetails(rainbowImage, 2, 2);
    }

    @Test
    public void testPaletteFactoryCountTransparentColors(){
        final PaletteFactory paletteFactory = new PaletteFactory();
        final BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < 5; x++) {
                image.setRGB(x, y, 0xff000000);
            }
            for (int x = 5; x < 10; x++) {
                image.setRGB(x, y, 0xff000001);
            }
        }
        // There are two transparent colors in the image. Return from the inner-most else.
        assertEquals(0, paletteFactory.countTransparentColors(image));

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < 5; x++) {
                image.setRGB(x, y, 0x00000000);
            }
            for (int x = 5; x < 10; x++) {
                image.setRGB(x, y, 0x00000001);
            }
        }
        // There are two transparent colors in the image. Return from the inner-most else.
        assertEquals(2, paletteFactory.countTransparentColors(image));

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < 3; x++) {
                image.setRGB(x, y, 0xFF000000);
            }
            for (int x = 3; x < 6; x++) {
                image.setRGB(x, y, 0xFF000001);
            }
            for (int x = 6; x < 10; x++) {
                image.setRGB(x, y, 0xFF000002);
            }
        }
        // return 0 when there is no transparent color. The second last if statement is covered.
        assertEquals(0, paletteFactory.countTransparentColors(image));


        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < 3; x++) {
                image.setRGB(x, y, 0x00000000);
            }
            for (int x = 3; x < 6; x++) {
                image.setRGB(x, y, 0xFF000001);
            }
            for (int x = 6; x < 10; x++) {
                image.setRGB(x, y, 0xFF000002);
            }
        }
        // return 1 when there is only one transparent color.
        assertEquals(1, paletteFactory.countTransparentColors(image));


        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < 3; x++) {
                image.setRGB(x, y, 0x00000000);
            }
            for (int x = 3; x < 6; x++) {
                image.setRGB(x, y, 0x00000001);
            }
            for (int x = 6; x < 10; x++) {
                image.setRGB(x, y, 0x00000002);
            }
        }

        // return 2 when more than one transparent color.
        assertEquals(2, paletteFactory.countTransparentColors(image));

        final BufferedImage rainbowImage = new BufferedImage(9, 10, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < 3; x++) {
                rainbowImage.setRGB(x, y, 0xFFFF0000);
            }
            for (int x = 3; x < 6; x++) {
                rainbowImage.setRGB(x, y, 0xFF00FF00);
            }
            for (int x = 6; x < 9; x++) {
                rainbowImage.setRGB(x, y, 0xFF0000FF);
            }
        }
        // return 0 when there is no transparent color. from the first if statement.
        assertEquals(0, paletteFactory.countTransparentColors(rainbowImage));



    }
}
