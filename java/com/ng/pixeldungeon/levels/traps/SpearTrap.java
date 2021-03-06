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
package com.ng.pixeldungeon.levels.traps;

import com.ng.pixeldungeon.Assets;
import com.ng.pixeldungeon.Dungeon;
import com.ng.pixeldungeon.ResultDescriptions;
import com.ng.pixeldungeon.actors.Actor;
import com.ng.pixeldungeon.actors.Char;
import com.ng.pixeldungeon.effects.Wound;
import com.ng.pixeldungeon.sprites.TrapSprite;
import com.ng.pixeldungeon.utils.GLog;
import com.ng.pixeldungeon.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class SpearTrap extends Trap {

	{
		name = "Spear trap";
		color = TrapSprite.GREY;
		shape = TrapSprite.DOTS;
	}

	@Override
	public void trigger() {
		if (Dungeon.visible[pos]){
			Sample.INSTANCE.play(Assets.SND_TRAP);
		}
		//this trap is not disarmed by being triggered
		reveal();
		activate();
	}

	@Override
	public void activate() {
		if (Dungeon.visible[pos]){
			Sample.INSTANCE.play(Assets.SND_HIT);
			Wound.hit(pos);
		}

		Char ch = Actor.findChar( pos);
		if (ch != null){
			int damage = Random.NormalIntRange(Dungeon.depth, Dungeon.depth*2);
			damage -= Random.IntRange( 0, ch.dr());
			ch.damage( Math.max(damage, 0) , this);
			if (!ch.isAlive() && ch == Dungeon.hero){
				Dungeon.fail(Utils.format(ResultDescriptions.TRAP, name));
				GLog.n("You were skewered by the spear trap...");
			}
		}
	}

	@Override
	public String desc() {
		return "The classic spear trap, primitive but effective. " +
				"Due to their simple nature, these traps can activate many times without breaking.";
	}
}
