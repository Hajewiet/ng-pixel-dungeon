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
package com.ng.pixeldungeon.actors.buffs;

import com.ng.pixeldungeon.actors.Char;
import com.ng.pixeldungeon.items.rings.RingOfElements.Resistance;
import com.ng.pixeldungeon.sprites.CharSprite;
import com.ng.pixeldungeon.ui.BuffIndicator;

public class Paralysis extends FlavourBuff {

	private static final float DURATION	= 10f;

	{
		type = buffType.NEGATIVE;
	}
	
	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo( target )) {
			target.paralysed++;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {
		super.detach();
		if (target.paralysed > 0)
			target.paralysed--;
	}
	
	@Override
	public int icon() {
		return BuffIndicator.PARALYSIS;
	}

	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add(CharSprite.State.PARALYSED);
		else target.sprite.remove(CharSprite.State.PARALYSED);
	}

	@Override
	public String toString() {
		return "Paralysed";
	}

	@Override
	public String desc() {
		return "Oftentimes the worst thing to do is nothing at all.\n" +
				"\n" +
				"Paralysis completely halts all actions, forcing the target to wait until the effect wears off. " +
				"The pain from taking damage  can also cause characters to snap out of paralysis.\n" +
				"\n" +
				"This paralysis will last for " + dispTurns() + ", or until it is resisted through pain.\n";
	}

	public static float duration( Char ch ) {
		Resistance r = ch.buff( Resistance.class );
		return r != null ? r.durationFactor() * DURATION : DURATION;
	}
}
