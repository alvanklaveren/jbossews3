package com.myforum.springframework;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myforum.gameshop.GameShopLogics;
import com.myforum.tables.GameConsole;

@Service
public class GameShopConsoleServiceImpl implements GameShopConsoleService{

	@Override
	public List<GameConsole> getConsoles(){   	
	    return GameShopLogics.getConsoleList();
    }
}