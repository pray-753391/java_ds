/*参考网站：https://blog.csdn.net/qq_37482202/article/details/89513877
 *广度优先搜索可以找到最短距离，它会彻底地搜索整张图，直到找到结果为止
 *广度搜索的一般步骤：
 *				1.使用图来建立问题模型
 *				2.使用广度优先搜索解决问题
 *广度搜索可以解决两类问题：
 *					1.从节点A出发，是否有前往节点B的路径
 *					解决方法：从节点A出发找最近的节点CD，如果都不是B，则把离CD最近的节点EF加入待搜索名单，直到找到B
 *					2.从节点A出发，前往节点B的哪条路径最短？
 *					解决方法：广度优先搜索会优先搜索离自己最近的，其次是离自己最近的节点离得最近的，所有当找到时，那条路线一定是最近的，但必须按添加顺序查找,
 *					而按照添加顺序查找，这个时候必须使用队列，这也正是队列这种数据结构被运用到的BFS算法中的原因。
 *广度优先算法的工作原理：
 *				1.创建一个队列，用于存储要检查的节点
 *				2.队列中弹出一个节点
 *				3.检查这个节点是否满足题目的条件
 *					是的话结束搜索，不是的话将离这个节点最近的节点全部加入队列
 *				4.重复第二步
 *				5.如果队列为空，说明查找失败
 *中途会出现这样的一个问题：
 *				C节点与B.D节点都是最近，那么它会被加入队列两次：当搜索B节点失败时，当搜索D节点失败时。
 *				而事实上只用检查B一次，所以当检查完一个节点后，应该将其标记为已检查，且不再检查他。
 *				目前，可以使用一个列表来记录已经检查了的人
 *在java里，通常使用hashmap来表示一个图，散列表可以将键映射到值，于是可以将节点映射到其所有邻居
 *运行时间：因为搜索了整个表，所以运行时间至少为O（边数），此外还使用了队列，其中包含每个要检查的节点。将每个节点固定加入队列的固定时间为O（1），所以对每个节点的时间复杂度为O（人数）
 *所以，广度优先搜索的运行时间为O（V+E）即，顶点数+边数
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//java一般使用LinkList表示队列，LinkedBlockingQueue是要求比较高时的队列了
import java.util.concurrent.LinkedBlockingQueue;


public class BFS {
	static class Node{
		//判断有无被搜索过
		public String id;
		//存储上一个节点（即是从哪个节点来的）用于之后打印路径
		public Node parent;
		//构造函数
		public Node(String id,Node parent) {
			this.id=id;
			this.parent=parent;
		}
	}
	
	
    //广度搜索的函数代码
    static Node findTarget(String startId,String targetId,HashMap<String,String[]> map) {
    	//用于存储已经搜索过的节点
    	 List<String> hasSearchList = new ArrayList<String>();
     	//创建搜索队列表，即主要进行BFS的队列
         LinkedBlockingQueue<Node> queue=new LinkedBlockingQueue<>();
         
         //add和offer的作用都是往队列末尾插入一个元素，区别在于：当超出队列界限的时候，add（）方法是抛出异常让你处理，而offer（）方法是直接返回false
         queue.offer(new Node(startId,null));
         //为空说明没有找到这个节点
         while(!queue.isEmpty()) {
        	 //poll peek element都能返回队列中首个元素。poll是直接弹出，另外两个只是查看。，为空时peek和poll会返回null，elementpoll会抛出异常
        	 Node node = queue.poll();
        	 //判断该节点是否已经被搜索过
        	 if(hasSearchList.contains(node.id)) continue;
        	 System.out.print("判断节点:" + node.id +"\n");
        	 //找到了的情况
        	 if (targetId.equals(node.id)) {
                 return node;
             }
        	 //没找到，把该节点标记为已搜索
        	 hasSearchList.add(node.id);
        	 
        	 //map里面搜索到了这个节点，且这个节点有最近节点
        	 if (map.get(node.id) != null && map.get(node.id).length > 0) {
                 //将这个节点的所有最近节点加入待搜索队列
        		 for (String childId : map.get(node.id)) {
        			 //将node作为前一个节点记录
                     queue.offer(new Node(childId,node));
                 }
        	 }
         }
         //不然就是没搜到
         return null;
    }
    //用于打印到达节点target所经过的各个节点信息
    static void printSearPath(Node target) {
    	if(target!=null) {
    		System.out.print("找到了目标节点:" + target.id + "\n");
    		//存储查找路径
    		List<Node> searchPath = new ArrayList<Node>();
//    		//先加入最后的target
//    		searchPath.add(target);
//    		//找到它的parent
//            Node node = target.parent;
            //循环找parent
            while(target!=null) {
                searchPath.add(target);
                target = target.parent;
            }
            //打印出来
            String path = "";
            for(int i=searchPath.size()-1;i>=0;i--) {
                path += searchPath.get(i).id;
                if(i!=0) {
                    path += "-->";
                }
            }
            System.out.print("步数最短："+path);
    	}else {
            System.out.print("未找到了目标节点");
        }
    }
    
	public static void main(String[] args) {
		//建立一个hashmap存储图
        HashMap<String,String[]> hashMap=new HashMap<>();
        
        //以下为图，第一个String是该节点名称，第二个String 是与之最近的节点
        hashMap.put("YOU",new String[]{"CLAIRE","ALICE","BOB"});
        hashMap.put("CLAIRE",new String[]{"YOU","JONNY","THON"});
        hashMap.put("JONNY",new String[]{"CLAIRE"});
        hashMap.put("THOH",new String[]{"CLAIRE"});
        hashMap.put("ALICE",new String[]{"YOU","PEGGY"});
        hashMap.put("BOB",new String[]{"YOU","PEGGY","ANUJ"});
        hashMap.put("PEGGY",new String[]{"BOB","ALICE"});
        hashMap.put("ANUJ",new String[]{"BOB"});
        //这个函数式求最短距离，第一个参数是开始节点，第二个参数是目标节点，第三个参数是图
        Node target = findTarget("YOU","ANUJ",hashMap);
        printSearPath(target);

	}
}
