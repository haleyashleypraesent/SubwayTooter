package jp.juggler.subwaytooter.api.entity

import jp.juggler.subwaytooter.api.TootParser
import jp.juggler.subwaytooter.util.LogCategory
import jp.juggler.subwaytooter.util.toJsonObject
import org.json.JSONObject
import java.util.regex.Pattern

object TootPayload {
	
	val log = LogCategory("TootPayload")
	
	private const val PAYLOAD = "payload"
	
	@Suppress("HasPlatformType")
	private val reNumber = Pattern.compile("([-]?\\d+)")
	
	// ストリーミングAPIのペイロード部分をTootStatus,TootNotification,整数IDのどれかに解釈する
	fun parsePayload(parser : TootParser, event : String, parent : JSONObject, parent_text : String) : Any? {
		try {
			if(parent.isNull(PAYLOAD)) {
				return null
			}
			
			val payload = parent.opt(PAYLOAD)
			
			if(payload is JSONObject) {
				when(event) {
					
					"update" ->
						// ここを通るケースはまだ確認できていない
						return parser.status(payload)
					
					"notification" ->
						// ここを通るケースはまだ確認できていない
						return parser.notification(payload)
					
					else -> {
						// ここを通るケースはまだ確認できていない
						log.e("unknown payload(1). message=%s", parent_text)
						return null
					}
				}
			}
			
			if(payload is Number) {
				// 2017/8/24 18:37 mastodon.juggler.jpでここを通った
				return payload.toLong()
			}
			
			if(payload is String) {
				
				if(payload[0] == '{') {
					val src = payload.toJsonObject()
					return when(event) {
						"update" ->
							// 2017/8/24 18:37 mastodon.juggler.jpでここを通った
							parser.status(src)
						
						"notification" ->
							// 2017/8/24 18:37 mastodon.juggler.jpでここを通った
							parser.notification(src)
						
						else -> {
							// ここを通るケースはまだ確認できていない
							log.e("unknown payload(2). message=%s", parent_text)
							null
						}
					}
				}
				// 2017/8/24 18:37 mdx.ggtea.org でここを通った
				val m = reNumber.matcher(payload)
				if(m.find()) {
					return m.group(1).toLong(10)
				}
			}
			
			// ここを通るケースはまだ確認できていない
			log.e("unknown payload(3). message=%s", parent_text)
			
		} catch(ex : Throwable) {
			log.trace(ex)
		}
		
		return null
	}
}
