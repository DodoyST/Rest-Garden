package com.example.restgarden.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AppResourceTest {
  @Test
  fun appResource_onSuccess() {
    val appResourceDummy = AppResource.Success("Success")
    assertThat(appResourceDummy.data).isNotNull()
    assertThat(appResourceDummy.data).isEqualTo("Success")
    assertThat(appResourceDummy.data).isNotEqualTo("Finished")
    assertThat(appResourceDummy.message).isNull()
  }
  
  @Test
  fun appResource_onError() {
    val appResourceDummy = AppResource.Error(null, "Error Occurred")
    assertThat(appResourceDummy.message).isNotNull()
    assertThat(appResourceDummy.message).isEqualTo("Error Occurred")
    assertThat(appResourceDummy.message).isNotEqualTo("Something Wrong")
  }
  
  @Test
  fun appResource_onLoading() {
    val appResourceDummy = AppResource.Loading
    assertThat((appResourceDummy.message)).isNull()
  }
}
