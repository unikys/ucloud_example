package com.kt.ucloud;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.json.JSONException;


import com.kt.ucloud.api.UcloudApiId;
import com.kt.ucloud.api.UcloudApiManager;

public class UcloudGUI extends JFrame{

	UcloudApiManager apiManager;
	TreeManager treeManager;
	JPanel mainPanel;
	/**
	 * 
	 */
	private static final long serialVersionUID = -2605499637935531895L;
	
	JLabel labelStatusBar;
	JLabel labelUser;

	public UcloudGUI (UcloudApiManager manager)
	{
		super("ucloud Example Application");
		
		this.apiManager = manager;
		
		setBounds(100,100,500,500);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		Container contentPane = this.getContentPane();
		
		
				
		mainPanel = new JPanel(new BorderLayout());
		
		this.renderToolBar();
		this.renderStatusBar();
		
		contentPane.add(mainPanel , BorderLayout.CENTER);
		
		setVisible(true);
	}
	
	private void renderStatusBar() {
		JPanel paneStatusBar = new JPanel(new BorderLayout());
		JLabel labelStatus = new JLabel("Status : ");
		
		labelStatusBar = new JLabel("");
		labelUser = new JLabel("Logged out");
		paneStatusBar.add(labelStatus , BorderLayout.WEST);
		paneStatusBar.add(labelStatusBar , BorderLayout.CENTER);
		paneStatusBar.add(labelUser, BorderLayout.EAST);
		
		mainPanel.add(paneStatusBar , BorderLayout.SOUTH);		
	}

	private void renderToolBar() {
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		
		ArrayList<JButton> toolbarButtonList = new ArrayList<JButton>();
		
		JButton buttonConnect = new JButton("연결");
		toolbarButtonList.add(buttonConnect);
		
		buttonConnect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setStatus("Connecting...");
				if(apiManager.connect())
				{
					setStatus("Connected");
				}else
				{
					setStatus("Error when connecting");
				}
				HashMap<?,?> result = apiManager.apiCall(UcloudApiId.GET_USER_INFO);

				String userName = (String)result.get("userName");
				setUser(userName);
				treeManager = new TreeManager(userName);

				setTreeRootInfo(result.get("Folders"));

				mainPanel.add(treeManager.getTree() , BorderLayout.CENTER);
				
			}

		});
		
		JButton buttonRefresh = new JButton("정보 읽기");
		toolbarButtonList.add(buttonRefresh);
		buttonRefresh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {

				String folderId = treeManager.getSelectedNode().getId();
				boolean isFolder = treeManager.getSelectedNode().isFolder();
				System.out.println("Selected node : " + folderId + " , " + isFolder);
				if(isFolder)
				{
					HashMap<String , String> params = new HashMap<String , String>();
					params.put("folder_id", folderId);
					HashMap<?,?> result = apiManager.apiCall(UcloudApiId.GET_CONTENTS, params);
					if(apiManager.isSuccess(result))
					{
						System.out.println(result + " \n " + result.get("Folders") + " \n "+ result.get("Files"));
						setTreeInfo(folderId , result.get("Folders") , result.get("Files"));
						setStatus("정보 읽기 완료");
					}else
					{
						setStatus("정보 읽기 실패");
					}
				}
			}
		});

		JButton buttonNewFolder = new JButton("새폴더");
		toolbarButtonList.add(buttonNewFolder);
		buttonNewFolder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {

				String folder_id = treeManager.getSelectedNode().getId();
				boolean isFolder = treeManager.getSelectedNode().isFolder();
				if(isFolder)
				{
					HashMap<String , String> params = new HashMap<String , String>();
					params.put("folder_id", folder_id);
					params.put("folder_name", "New");
					HashMap<?,?> result = apiManager.apiCall(UcloudApiId.CREATE_FOLDER , params);
					System.out.println(result);
				}
				
				
			}
		});

		JButton buttonDeleteFolder = new JButton("폴더 지우기");
		toolbarButtonList.add(buttonDeleteFolder);
		buttonDeleteFolder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {

				String folder_id = treeManager.getSelectedNode().getId();
				boolean isFolder = treeManager.getSelectedNode().isFolder();
				if(isFolder)
				{
					HashMap<String , String> params = new HashMap<String , String>();
					params.put("folder_id", folder_id);
					HashMap<?,?> result = apiManager.apiCall(UcloudApiId.DELETE_FOLDER , params);
					System.out.println(result);
				}
				
				
			}
		});

		
		JButton buttonUploadFile = new JButton("파일 업로드");
		toolbarButtonList.add(buttonUploadFile);
		buttonUploadFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {

				String folder_id = treeManager.getSelectedNode().getId();
				boolean isFolder = treeManager.getSelectedNode().isFolder();
				if(isFolder)
				{
					HashMap<String , String> params = new HashMap<String , String>();
					params.put("folder_id", folder_id);
					HashMap<?,?> result = apiManager.apiCall(UcloudApiId.UPLOAD_FILE , params);
					System.out.println(result);
				}
			}
		});
		
		
		JButton buttonDownloadFile = new JButton("파일 다운로드");
		toolbarButtonList.add(buttonDownloadFile);
		buttonDownloadFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {

				String folder_id = treeManager.getSelectedNode().getId();
				boolean isFolder = treeManager.getSelectedNode().isFolder();
				if(isFolder)
				{
					HashMap<String , String> params = new HashMap<String , String>();
					params.put("folder_id", folder_id);
					HashMap<?,?> result = apiManager.apiCall(UcloudApiId.DOWNLOAD_FILE , params);
					System.out.println(result);
				}
				
				
			}
		});

		
		
		
		for(JButton button : toolbarButtonList)
			toolbar.add(button);
		
		mainPanel.add(toolbar , BorderLayout.NORTH);
	}

	public void setStatus(String status)
	{
		this.labelStatusBar.setText(status);
	}
	
	public void setUser(String user)
	{
		this.labelUser.setText("Login(" + user + ")");
	}
	
	
	public void setTreeInfo(String id , Object folders , Object files)
	{
		for(org.json.JSONObject folderInfo : (ArrayList<org.json.JSONObject>)folders)
		{
			this.treeManager.addFolderToNode(id , folderInfo);
		}
		for(org.json.JSONObject fileInfo : (ArrayList<org.json.JSONObject>)files)
		{
			this.treeManager.addFileToNode(id , fileInfo);
		}
	}
	
	public void setTreeRootInfo(Object folders) {
		for(org.json.JSONObject folderInfo : (ArrayList<org.json.JSONObject>)folders)
		{
			this.treeManager.addFolderToNode(null , folderInfo);
		}
	}
	
}
