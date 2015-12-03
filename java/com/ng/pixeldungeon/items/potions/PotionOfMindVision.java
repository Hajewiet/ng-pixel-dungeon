/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.ng.pixeldungeon.items.potions;

import com.ng.pixeldungeon.Dungeon;
import com.ng.pixeldungeon.actors.buffs.Buff;
import com.ng.pixeldungeon.actors.buffs.MindVision;
import com.ng.pixeldungeon.actors.hero.Hero;
import com.ng.pixeldungeon.utils.GLog;

public class PotionOfMindVision extends Potion {

	{
		name = "Potion of Mind Vision";
		initials = "MV";
	}
	
	@Override
	public void apply( Hero hero ) {
		setKnown();
		Buff.affect( hero, MindVision.class, MindVision.DURATION );
		Dungeon.observe();
		
		if (Dungeon.level.mobs.size() > 0) {
			GLog.i( "You can somehow feel the presence of other creatures' minds!" );
		} else {
			GLog.i( "You can somehow tell that you are alone on this level at the moment." );
		}
	}
	
	@Override
	public String desc() {
		return
			"After drinking this, your mind will become attuned to the psychic signature " +
			"of distant creatures, enabling you to sense biological presences through walls. " +
			"Also this potion will permit you to see through nearby walls and doors.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 35 * quantity : super.price();
	}
}
