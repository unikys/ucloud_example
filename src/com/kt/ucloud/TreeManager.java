package com.kt.ucloud;

import java.awt.Component;
import java.util.HashMap;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.json.JSONException;

public class TreeManager {

	JTree tree;
	JScrollPane treeScrollPane;
	DefaultMutableTreeNode root;
	
	public TreeManager(String user)
	{
		
		root = new DefaultMutableTreeNode(user);

		tree = new JTree(root);
		tree.setCellRenderer(new TreeFolderCellRenderer());

		treeScrollPane = new JScrollPane(tree);
		
	}

	public void addNode(String parentId , String childId , String childName , boolean expandable)
	{		
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(new TreeNodeInformation(childId , childName , expandable) , expandable);
		if(parentId == null)
		{
			root.add(node);
		}else
		{
			((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).add(node);
		}
		tree.revalidate();
		
	}
	
	public void addFolderToNode(String id , org.json.JSONObject folderInfo)
	{
		String folderName = "" , folderId = "";
		try {
			folderName = folderInfo.getString("folder_name");
			folderId = folderInfo.getString("folder_id");
		} catch (JSONException e) {
			e.printStackTrace();
			System.err.println("Error : parsing folderInfo - " + folderInfo);
		}
		this.addNode(id, folderId, folderName, true);
	}

	public void addFileToNode(String id , org.json.JSONObject fileInfo)
	{
		String fileName = "" , fileId = "";
		try {
			fileName = fileInfo.getString("file_name");
			fileId = fileInfo.getString("file_id");
		} catch (JSONException e) {
			e.printStackTrace();
			System.err.println("Error : parsing fileInfo - " + fileInfo);
		}
		this.addNode(id, fileId, fileName, false);
	}

	public JScrollPane getTree()
	{
		return this.treeScrollPane;
	}
	
	public TreeNodeInformation getSelectedNode()
	{
		return (TreeNodeInformation)((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject();
	}
	
	
	public class TreeNodeInformation
	{
		private String id;
		private String name;
		private boolean isFolder;
		
		public TreeNodeInformation(String id , String name , boolean isFolder)
		{
			this.setId(id);
			this.setName(name);
			this.setFolder(isFolder);
		}
		
		@Override
		public String toString() {
			return getName();
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isFolder() {
			return isFolder;
		}

		public void setFolder(boolean isFolder) {
			this.isFolder = isFolder;
		}
	}
	
	public class CustomTreeModel extends DefaultTreeModel
	{

		public CustomTreeModel(TreeNode root, boolean asksAllowsChildren) {
			super(root, asksAllowsChildren);
		}
		
	}
	
	public class TreeFolderCellRenderer extends DefaultTreeCellRenderer
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -5142123092830530692L;

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
					hasFocus);

			Object userObject = ((DefaultMutableTreeNode)value).getUserObject(); 
			if(userObject instanceof TreeNodeInformation)
			{
				if(((TreeNodeInformation)userObject).isFolder())
				{
					setIcon(MetalIconFactory.getTreeFolderIcon());
				}else
				{
					setIcon(MetalIconFactory.getTreeLeafIcon());
				}
			}else
			{
				setIcon(MetalIconFactory.getTreeFolderIcon());
			}
			return this;
		}
	}

}
