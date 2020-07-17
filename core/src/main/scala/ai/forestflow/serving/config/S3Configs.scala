package ai.forestflow.serving.config

import java.util.concurrent.TimeUnit

object S3Configs {
  import ApplicationEnvironment.config
  lazy val s3Configs = config.getConfig("application.s3credentials")
  lazy val S3_DOWNLOAD_TIMEOUT_SECS = {
    val duration = s3Configs.getDuration("download-timeout", TimeUnit.SECONDS)
    require(duration > 1, "s3-download-timeout cannot be less than 1 second")
    duration
  }
  lazy val S3_ACCESS_KEY_ID_POSTFIX = s3Configs.getString("access-key-id-postfix")
  lazy val S3_SECRET_ACCESS_KEY_POSTFIX = s3Configs.getString("secret-access-key-postfix")
}
