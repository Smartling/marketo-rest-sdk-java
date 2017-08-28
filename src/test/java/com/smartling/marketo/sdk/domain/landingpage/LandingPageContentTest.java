package com.smartling.marketo.sdk.domain.landingpage;

import com.smartling.it.marketo.sdk.testutils.TestUtils;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.extractProperty;

public class LandingPageContentTest
{

    @Test
    public void ordinaryContent() {
        List<LandingPageContentItem> items = TestUtils.fromJsonListByClassLoader(
                "json/ordinary_content.json", LandingPageContentItem.class
        );

        assertThat(items).hasSize(6);
        assertThat(items.get(5)).isInstanceOf(LandingPageTextContentItem.class);
    }

    @Test
    public void dynamicContent() {
        List<LandingPageTextContentItem> items = TestUtils.fromJsonListByClassLoader(
                "json/dynamic_content.json", LandingPageTextContentItem.class
        );

        final String nullContent = null;
        assertThat(extractProperty("content", String.class).from(items)).containsExactly(
                nullContent, nullContent, "plain content"
        );
    }

}
