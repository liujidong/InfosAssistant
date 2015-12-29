package com.eoemobile.infos_assistant;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class FSExplorer extends Activity implements OnItemClickListener {
	private static final String TAG = "FSExplorer";
	//private static final int IM_PARENT = Menu.FIRST + 1;
	//private static final int IM_BACK = IM_PARENT + 1;
 
	ListView itemlist = null;
	String path = Environment.getExternalStorageDirectory().getPath();
	List<Map<String, Object>> list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.files);
		setTitle("文件浏览器");
		itemlist = (ListView) findViewById(R.id.itemlist);
		refreshListItems(path);
	}

	private void refreshListItems(String path) {
		setTitle("文件浏览器 > "+path);
		list = buildListForSimpleAdapter(path);
		SimpleAdapter notes = new SimpleAdapter(this, list, R.layout.file_row,
				new String[] { "name", "time" ,"img"}, new int[] { R.id.name,
						R.id.desc ,R.id.img});
		itemlist.setAdapter(notes);
		itemlist.setOnItemClickListener(this);
		itemlist.setSelection(0);
	}
	//按照文件名称排序
	public static void orderByName(List files) {
	  //List files = Arrays.asList(new File(fliePath).listFiles());
	  Collections.sort(files, new Comparator<File>() {
	   @Override
	   public int compare(File o1, File o2) {
		if (o1.isDirectory() && o2.isFile())
	          return -1;
		if (o1.isFile() && o2.isDirectory())
	          return 1;
		return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
	   }
	  });
	} 
	private List<Map<String, Object>> buildListForSimpleAdapter(String path) {
		File[] files0 = new File(path).listFiles();
		List<File> files = Arrays.asList(files0);
		orderByName(files);
		list = new ArrayList<Map<String, Object>>(files0.length);
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("name", path);
		root.put("img", R.drawable.file_root);
		root.put("time", "返回顶级目录（go to root directory）");
		list.add(root);
		Map<String, Object> pmap = new HashMap<String, Object>();
		pmap.put("name", "..");
		pmap.put("img", R.drawable.file_paranet);
		pmap.put("time", "返回父级目录（go to paranet Directory）");
		list.add(pmap);
		Date fd = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (File file : files){
			Map<String, Object> map = new HashMap<String, Object>();
			if(file.isDirectory()){
				map.put("img", R.drawable.directory);
			}else{
				map.put("img", R.drawable.file_doc);
			}
			map.put("name", file.getName());
			fd = new Date(file.lastModified());
			map.put("time", sdf.format(fd));
			
			list.add(map);
		}
		return list;
	}
	
	private void goToParent() {
		File file = new File(path);
		File str_pa = file.getParentFile();
		if(str_pa == null){
			Toast.makeText(FSExplorer.this,
					getString(R.string.is_root_dir),
					Toast.LENGTH_SHORT).show();
			refreshListItems(path);	
		}else{
			path = str_pa.getAbsolutePath();
			refreshListItems(path);	
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Log.i(TAG, "item clicked! [" + position + "]");
		if (position == 0) {
			path = Environment.getExternalStorageDirectory().getPath();
			refreshListItems(path);
		}else if(position == 1){
			goToParent();
		} else {
			//path = (String) list.get(position).get("path");
			path = path + File.separator + list.get(position).get("name");
			File file = new File(path);
			if (file.isDirectory())
				refreshListItems(path);
			else
				Toast.makeText(FSExplorer.this,
						getString(R.string.is_file),
						Toast.LENGTH_SHORT).show();
		}

	}

 

}
