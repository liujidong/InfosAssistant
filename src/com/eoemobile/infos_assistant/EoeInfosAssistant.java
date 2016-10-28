package com.eoemobile.infos_assistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import person.ljd.infos_assistant.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class EoeInfosAssistant extends Activity implements OnItemClickListener {
	private static final String TAG = "eoeInfosAssistant";
	ListView itemlist = null;
	List<Map<String, Object>> list;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		itemlist = (ListView) findViewById(R.id.itemlist);
		refreshListItems();
		//-------------------广告-------------------------------
		//分别为应用的发布 ID 和密钥
		AdManager.getInstance(this).init("e98cb7cf099e4472", "765e6dd0273dc5e5", false);
		showBanner();

	}
	private void showBanner() {
		// 实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		/*
		// 获取要嵌入广告条的布局
		LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);
		// 将广告条加入到布局中
		adLayout.addView(adView);	
		*/
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		// 设置广告条的悬浮位置
		layoutParams.gravity = Gravity.BOTTOM; // 这里示例为右下角
		this.addContentView(adView, layoutParams);
	}
	private void refreshListItems() {
		list = buildListForSimpleAdapter();
		SimpleAdapter notes = new SimpleAdapter(this, list, R.layout.item_row,
				new String[] { "name", "desc", "img" }, new int[] { R.id.name,
						R.id.desc, R.id.img });
		itemlist.setAdapter(notes);
		itemlist.setOnItemClickListener(this);
		itemlist.setSelection(0);
	}

	private List<Map<String, Object>> buildListForSimpleAdapter() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(3);
		// Build a map for the attributes
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "系统信息");
		map.put("desc", "查看设备系统版本,运营商及其系统信息.");
		map.put("img", R.drawable.system);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("name", "硬件信息");
		map.put("desc", "查看包括CPU,硬盘,内存等硬件信息.");
		map.put("img", R.drawable.hardware);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("name", "软件信息");
		map.put("desc", "查看已经安装的软件信息.");
		map.put("img", R.drawable.software);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("name", "运行时信息");
		map.put("desc", "查看设备运行时的信息.");
		map.put("img", R.drawable.running);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("name", "文件浏览器");
		map.put("desc", "浏览查看文件系统.");
		map.put("img", R.drawable.file_explorer);
		list.add(map);
		
		return list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Intent intent = new Intent();
		Log.i(TAG, "item clicked! [" + position + "]");
		switch (position) {
		case 0:
			intent.setClass(EoeInfosAssistant.this, System.class);
			startActivity(intent);
			break;
		case 1:
			intent.setClass(EoeInfosAssistant.this, Hardware.class);
			startActivity(intent);
			break;
		case 2:
			intent.setClass(EoeInfosAssistant.this, Software.class);
			startActivity(intent);
			break;
		case 3:
			intent.setClass(EoeInfosAssistant.this, Runing.class);
			startActivity(intent);
			break;
		case 4:
			intent.setClass(EoeInfosAssistant.this, FSExplorer.class);
			startActivity(intent);
			break;
		}
	}
}