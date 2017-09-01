package com.myforum.springframework;

import java.util.List;

import com.myforum.tables.GameConsole;

public interface GameShopConsoleService {
	
	List<GameConsole> getConsoles();
}
