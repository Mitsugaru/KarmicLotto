package com.mitsugaru.KarmicLotto.permissions;

public enum Permission
{
	ADMIN(".admin"), USE(".use"), VIEW(".view"), CREATE(".create");

	private static final String prefix = "KarmicLotto";
	private String node;

	private Permission(String node)
	{
		this.node = prefix + node;
	}

	public String getNode()
	{
		return node;
	}

}
