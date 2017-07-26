package jp.juggler.subwaytooter.api.entity;

import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.TextUtils;

public abstract class TootStatusLike extends TootId {
	
	//URL to the status page (can be remote)
	public String url;
	
	public String host_original;
	public String host_access;
	
	// The TootAccount which posted the status
	@Nullable public TootAccount account;
	
	//The number of reblogs for the status
	public long reblogs_count;
	
	//The number of favourites for the status
	public long favourites_count;
	
	//	Whether the authenticated user has reblogged the status
	public boolean reblogged;
	
	//	Whether the authenticated user has favourited the status
	public boolean favourited;
	
	//Whether media attachments should be hidden by default
	public boolean sensitive;
	
	//If not empty, warning text that should be displayed before the actual content
	public String spoiler_text;
	public Spannable decoded_spoiler_text;
	
	//	Body of the status; this will contain HTML (remote HTML already sanitized)
	public String content;
	public Spannable decoded_content;
	
	//Application from which the status was posted
	@Nullable public TootApplication application;
	
	public long time_created_at;
	
	public abstract boolean hasMedia();
	
	public String getHostAccessOrOriginal(){
		return ( TextUtils.isEmpty( host_access ) || "?".equals( host_access ) ) ? host_original : host_access;
	}
	
	public long getIdAccessOrOriginal(){
		return id;
	}
	
	public String getBusyKey(){
		return getHostAccessOrOriginal() + ":" + getIdAccessOrOriginal();
	}
}
