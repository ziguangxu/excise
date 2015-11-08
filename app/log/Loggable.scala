package log

import play.api.Logger

/**
 * @author Ziguang Xu
 */
trait Loggable {
    @transient
    val log: Logger = Logger(this.getClass())
}
