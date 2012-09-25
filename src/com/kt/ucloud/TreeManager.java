package com.kt.ucloud;

import java.awt.Component;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
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
	
	public void addNewNodeToNode(String parentId , org.json.JSONObject folderInfo, boolean isFolder)
	{
		String name = "" , id = "";
		try {
			if(isFolder)
			{
				name = folderInfo.getString("folder_name");
				id = folderInfo.getString("folder_id");
			}else
			{
				name = folderInfo.getString("file_name");
				id = folderInfo.getString("file_id");				
			}
		} catch (JSONException e) {
			e.printStackTrace();
			System.err.println("Error : parsing folderInfo - " + folderInfo);
		}
		this.addNode(parentId, id, name, isFolder);
		tree.revalidate();
	}

	public JScrollPane getTree()
	{
		return this.treeScrollPane;
	}
	
	public TreeNodeInformation getSelectedNode()
	{
		return (TreeNodeInformation)((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject();
	}
	
	public void removeSelectedNode()
	{
		((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).removeFromParent();		
		tree.revalidate();
	}
	
	//트리의 노드가 가지고 있을 정보는 id, name, isFolder
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
	
	//빈 폴더는 isLeaf() == true 이기 때문에 폴더모양 아이콘으로 바뀌주기 위함
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
