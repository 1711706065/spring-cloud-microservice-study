package com.lmc.myrules;

import java.util.List;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

/**
 * 自定义负载均衡轮询算法，每个微服务轮询3次再切换下一台微服务
 * @author lmc
 *
 */
public class MyRule_LMC extends AbstractLoadBalancerRule{
	
	//每个微服务轮询次数
	private static final int ROUND_NUM = 3;
	//当前被调用的次数
	private int total = 0;
	//当前的微服务索引
	private int currentIndex = 0;
	
	public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        Server server = null;

        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> upList = lb.getReachableServers();
            List<Server> allList = lb.getAllServers();

            int serverCount = allList.size();
            if (serverCount == 0) {
                /*
                 * No servers. End regardless of pass, because subsequent passes
                 * only get more restrictive.
                 */
                return null;
            }

            if(total < ROUND_NUM){
            	server = upList.get(currentIndex);
            	++total;
            }else {
            	total = 0;
            	++currentIndex;
            	if(currentIndex >= upList.size()){
            		currentIndex = 0;
            	}
            }

            if (server == null) {
                /*
                 * The only time this should happen is if the server list were
                 * somehow trimmed. This is a transient condition. Retry after
                 * yielding.
                 */
                Thread.yield();
                continue;
            }

            if (server.isAlive()) {
                return (server);
            }

            // Shouldn't actually happen.. but must be transient or a bug.
            server = null;
            Thread.yield();
        }

        return server;

    }

	@Override
	public Server choose(Object key) {
		return choose(getLoadBalancer(), key);
	}

	@Override
	public void initWithNiwsConfig(IClientConfig clientConfig) {
		// TODO Auto-generated method stub
		
	}

}
