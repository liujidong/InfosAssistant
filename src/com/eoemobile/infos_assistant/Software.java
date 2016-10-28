package com.eoemobile.infos_assistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import person.ljd.infos_assistant.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class Software extends Activity implements  Runnable {
	List<Map<String, Object>> list = null;
	ListView itemlist = null;
	 private ProgressDialog pd;
	 
	@Override
	public void onCreate(Bundle icicle) {  
        super.onCreate(icicle);  
		setContentView(R.layout.infos);
		setTitle("软件信息");
		itemlist = (ListView) findViewById(R.id.itemlist);
        pd = ProgressDialog.show(this, "请稍候..", "正在收集你已经安装的软件信息...", true,
                false);
        Thread thread = new Thread(this);
        thread.start();
    }  
	
 
	private void refreshListItems() {
		list = fetch_installed_apps2();
		SimpleAdapter notes = new SimpleAdapter(this, list, R.layout.info_row,
				new String[] { "name", "desc" }, new int[] { R.id.name,
						R.id.desc });
		itemlist.setAdapter(notes);
		setTitle("软件信息,已经安装"+list.size()+"款应用.");
	}
	
	
	/*
	public List fetch_installed_apps(){
        List<ApplicationInfo> packages = getPackageManager().getInstalledApplications(0);
		list = new ArrayList<Map<String, Object>>(
				packages.size());
		Iterator<ApplicationInfo> l = packages.iterator();
		
		while (l.hasNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			ApplicationInfo app = (ApplicationInfo) l.next();
			String packageName = app.packageName;
            String label = "";
            try {
                label = getPackageManager().getApplicationLabel(app).toString();
            } catch (Exception e) {  
            	Log.i("Exception",e.toString());
            }
    		map = new HashMap<String, Object>();
     		map.put("name", label);
    		map.put("desc", packageName);
    		list.add(map);
		}
		return list;
    }*/
	public List fetch_installed_apps2(){
		List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
		list = new ArrayList<Map<String, Object>>(packages.size());		        
		for(int i=0;i<packages.size();i++) { 
		        PackageInfo packageInfo = packages.get(i);  
		        String label = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString(); 
		        String packageName = packageInfo.packageName; 
		        String versionName = packageInfo.versionName; 
		        //tmpInfo.versionCode = packageInfo.versionCode; 
		        //tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(getPackageManager());
		        //Only display the non-system app info
		        if((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0)
		        {
		        	Map<String, Object> map = new HashMap<String, Object>();
		     		map.put("name", label+"("+versionName+")");
		    		map.put("desc", packageName);
		    		list.add(map);
		        }
		        
		 }	
		return list;
	}	
	@Override
	public void run() {
    	fetch_installed_apps2();
       handler.sendEmptyMessage(0);
	}
	
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            refreshListItems();
            pd.dismiss();
        }
    };
    
}
