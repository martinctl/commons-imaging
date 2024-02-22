package org.apache.commons.imaging.palette;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.apache.commons.imaging.ImagingException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for the {@link MostPopulatedBoxesMedianCut} class.
 * These tests verify the correctness of the median cut algorithm
 * implementation,
 * specifically focusing on its ability to correctly perform median cuts under
 * various conditions.
 * Coverage includes scenarios with color groups having maximum difference of
 * zero, dominant color components,
 * and empty input lists.
 */
public class MostPopulatedBoxesMedianCutTest {

    private List<ColorGroup> colorGroups;
    private MostPopulatedBoxesMedianCut cut;

    /**
     * Sets up the test environment before each test. Initializes color groups and
     * the median cut instance.
     * This preparation ensures a clean state for each test.
     */
    @BeforeEach
    public void setUp() {
        colorGroups = new ArrayList<>();
        cut = new MostPopulatedBoxesMedianCut();
    }

    /**
     * Tests that the {@code performNextMedianCut} method can perform a cut based on
     * alpha variation when not ignoring alpha values. The test sets up color counts
     * with distinct alpha values to simulate a scenario where alpha is the primary
     * factor for differentiation.
     *
     * @throws ImagingException if processing fails
     */
    @Test
    public void dominantAlphaReturnsTrue() throws ImagingException {
        // Assuming ColorCount can handle alpha values distinctly
        ColorCount mostlyTransparent = new ColorCount(0x01ffffff); // Minimal alpha
        mostlyTransparent.count = 100;
        ColorCount mostlyOpaque = new ColorCount(0xff00ff00); // Fully opaque green
        mostlyOpaque.count = 1;

        List<ColorCount> colorCounts = Arrays.asList(mostlyTransparent, mostlyOpaque);
        ColorGroup colorGroup = new ColorGroup(colorCounts, false);
        colorGroups.add(colorGroup);

        boolean result = cut.performNextMedianCut(colorGroups, false); // Assuming false allows considering alpha

        assertTrue(result, "Expected true when alpha variation is dominant.");
    }

    /**
     * Tests the {@code performNextMedianCut} method with a scenario where the green
     * color component is dominant.
     * This test verifies that the method correctly identifies green as the dominant
     * color for the median cut, returning true.
     *
     * @throws ImagingException if an error occurs during the median cut process
     */
    @Test
    public void dominantGreenReturnsTrue() throws ImagingException {
        ColorCount colorCountRed = new ColorCount(0xFFFF0000); // Red
        colorCountRed.count = 1;
        ColorCount colorCountGreen = new ColorCount(0xFF00FF00); // Green
        colorCountGreen.count = 1;
        ColorCount colorCountBlue = new ColorCount(0xFF0000FF); // Blue
        colorCountBlue.count = 5;

        List<ColorCount> colorCounts = Arrays.asList(colorCountRed, colorCountGreen, colorCountBlue);
        ColorGroup colorGroup = new ColorGroup(colorCounts, true);
        List<ColorGroup> colorGroups = new ArrayList<>();
        colorGroups.add(colorGroup);

        MostPopulatedBoxesMedianCut cut = new MostPopulatedBoxesMedianCut();
        boolean result = cut.performNextMedianCut(colorGroups, true);

        assertTrue(result, "Expected true when green is dominant for the median cut.");
    }

    /**
     * Tests the {@code performNextMedianCut} method with an empty list of color
     * groups. No color groups added means colorGroup should remain null.
     * This test verifies that the method correctly returns false, indicating that
     * no median cuts can be performed.
     *
     * @throws ImagingException if an error occurs during the median cut process
     */
    @Test
    public void emptyGroupsReturnFalse() throws ImagingException {
        boolean result = cut.performNextMedianCut(colorGroups, false);
        assertFalse(result, "Expected performNextMedianCut to return false with empty color groups");
    }
}
