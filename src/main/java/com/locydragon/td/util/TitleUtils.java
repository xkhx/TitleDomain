package com.locydragon.td.util;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

/**
 * Title工具类
 *
 * @author May_Speed, D_xiaox
 * @since 2016/12/26
 * <p>
 * update 2017/8/05
 * update 2018/8/02 星空(D_xiaox)
 */
public class TitleUtils {

	/**
	 * 给一个玩家发送Title信息 1.8+
	 *
	 * @param player   发送的玩家
	 * @param fadeIn   淡入时间
	 * @param stay     停留时间
	 * @param fadeOut  淡出时间
	 * @param title    主标题
	 * @param subTitle 副标题
	 */
	public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title,
								 String subTitle) {
		// 获取PL管理
		ProtocolManager pm = ProtocolLibrary.getProtocolManager();
		PacketContainer packet = null;
		if (title != null) {
			title = ChatColor.translateAlternateColorCodes('&', title); // 支持&颜色代码
			title = title.replaceAll("%player%", player.getName());
			// 创建标题数据包
			packet = pm.createPacket(PacketType.Play.Server.TITLE);
			// nms内封包结构为
			/*
			 * private EnumTitleAction a; private IChatBaseComponent b; private int c;
			 * private int d; private int e;
			 */
			// 按顺序往里写入数据
			packet.getTitleActions().write(0, EnumWrappers.TitleAction.TITLE); // EnumTitleAction
			packet.getChatComponents().write(0, WrappedChatComponent.fromText(title)); // 标题内容
			try {
				pm.sendServerPacket(player, packet, false); // 发送数据包
			} catch (InvocationTargetException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}

		if (subTitle != null) {
			subTitle = ChatColor.translateAlternateColorCodes('&', subTitle); // 支持&颜色代码
			subTitle = subTitle.replaceAll("%player%", player.getName());
			packet = pm.createPacket(PacketType.Play.Server.TITLE);
			packet.getTitleActions().write(0, EnumWrappers.TitleAction.SUBTITLE);
			packet.getChatComponents().write(0, WrappedChatComponent.fromText(subTitle));
			try {
				pm.sendServerPacket(player, packet, false);
			} catch (InvocationTargetException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}

		packet = pm.createPacket(PacketType.Play.Server.TITLE);
		packet.getTitleActions().write(0, EnumWrappers.TitleAction.TIMES);
		packet.getIntegers().write(0, fadeIn); // ---> c
		packet.getIntegers().write(1, stay); // ---> d
		packet.getIntegers().write(2, fadeOut); // ---> e
		try {
			pm.sendServerPacket(player, packet, false); // 发送数据包
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
