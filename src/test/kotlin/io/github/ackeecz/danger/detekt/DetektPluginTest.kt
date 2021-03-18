package io.github.ackeecz.danger.detekt

import com.google.common.truth.Truth
import org.junit.Test
import java.io.File

class DetektPluginTest {

    @Test
    fun `should process two files in report`() {
        // before
        val context = FakeDangerContext()
        DetektPlugin.context = context
        // when
        DetektPlugin.parseAndReport(File(ClassLoader.getSystemResource("detekt_result_two_files.xml").toURI()))
        // then
        Truth.assertThat(context.warnings)
            .hasSize(6)
    }

    @Test
    fun `should process two report files`() {
        // before
        val context = FakeDangerContext()
        DetektPlugin.context = context
        // when
        DetektPlugin.parseAndReport(
            File(ClassLoader.getSystemResource("detekt_result_two_files.xml").toURI()),
            File(ClassLoader.getSystemResource("detekt_result_single_file.xml").toURI())
        )
        // then
        Truth.assertThat(context.warnings)
            .hasSize(8)
    }

    @Test
    fun `should process one file in report`() {
        // before
        val context = FakeDangerContext()
        DetektPlugin.context = context
        // when
        DetektPlugin.parseAndReport(
            File(ClassLoader.getSystemResource("detekt_result_single_file.xml").toURI())
        )
        // then
        Truth.assertThat(context.warnings)
            .hasSize(2)
    }

    @Test
    fun `should process no file in report`() {
        // before
        val context = FakeDangerContext()
        DetektPlugin.context = context
        // when
        DetektPlugin.parseAndReport(
            File(ClassLoader.getSystemResource("detekt_result_no_files.xml").toURI())
        )
        // then
        Truth.assertThat(context.warnings)
            .isEmpty()
    }

    @Test
    fun `should have relative path in filename`() {
        // before
        val context = FakeDangerContext()
        DetektPlugin.context = context
        // when
        DetektPlugin.parseAndReport(
            File(ClassLoader.getSystemResource("detekt_result_single_file.xml").toURI())
        )
        // then
        Truth.assertThat(context.warnings.first().file)
            .isEqualTo("features/recipelist/src/main/java/cz/ackee/sample/recipelist/presentation/RecipeListFragment.kt")
    }
}