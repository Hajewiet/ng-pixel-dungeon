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
package com.ng.pixeldungeon.actors.mobs;

import com.ng.pixeldungeon.Dungeon;
import com.ng.pixeldungeon.actors.buffs.Buff;
import com.ng.pixeldungeon.actors.buffs.Chill;
import com.ng.pixeldungeon.actors.buffs.Frost;
import com.ng.pixeldungeon.items.quest.Embers;
import com.ng.pixeldungeon.sprites.NewbornElementalSprite;

public class NewbornElemental extends Elemental {

	{
		name = "newborn fire elemental";
		spriteClass = NewbornElementalSprite.class;

		HT = 65;
		HP = HT/2; //32

		defenseSkill = 12;

		EXP = 7;

	}

	@Override
	public int damageRoll() {
		return super.damageRoll()/2;
	} //8-10

	@Override
	public void add(Buff buff) {
		if (buff instanceof Frost || buff instanceof Chill) {
			die(buff);
		} else {
			super.add(buff);
		}
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
		Dungeon.level.drop( new Embers(), pos ).sprite.drop();
	}

	@Override
	public String description() {
		return "Fire elementals are a byproduct of summoning greater entities. " +
				"They are too chaotic in their nature to be controlled by even the most powerful demonologist.\n\n" +
				"This fire elemental is freshy summoned, and is weakened as a result. " +
				"In this state is it especially vulnerable to the cold. " +
				"Its offensive capabilities are still great though, caution is advised.";
	}
}
