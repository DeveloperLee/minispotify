package com.lzh.constants;

public class StateConstant {
	
     	 public static final String PLAY  = "music.play";
     	 public static final String PAUSE = "music.pause";
     	 public static final String STOP = "music.stop";
     	 public static final String PREVIOUS_SONG = "music.previous.song";
     	 public static final String NEXT_SONG = "music.next.song";
         public static final String PREPARE = "music.prepare";
         public static final String AUTO_NEXT_SONG = "music.auto.next.song";
         public static final String SEND_LIST_POSITION = "music.list.position";    //传递播放列表选择项位置到Service中的action_name
         public static final String SEND_POSTION_FROM_ACTIVITY = "music.position.from.activity";
         public static final String SEND_POSITION_FROM_SERVICE = "music.position.from.service";
         public static final String SEND_MODE_FROM_ACTIVITY = "music.mode.from.activity";
         public static final String SEND_MUSIC_INFO_FROM_SERVICE = "music.info.from.service";
         public static final String CHANGE_SONG_SET = "music.change.song.set";
         public static final int SERVICE_PLAYING = 0;
         public static final int SERVICE_PAUSE = 1;
     	 public static final int STOP_REFRESH = 2;
     	 public static final int SHOW_SETTINGS_ANIM = 3;
     	 public static final int REFRESH = 4;
     	 public static final int BOTTOM_REFRESH = 5;
     	 public static final int HOME_TOP_REFRESH = 6;
     	 public static final int LOAD_HEAD_CACHE = 7;
		 public static String send_POSITION_FROM_ARTIST = "music.position.from.artist";
		 
}
