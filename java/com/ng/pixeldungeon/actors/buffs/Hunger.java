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

import com.ng.pixeldungeon.Badges;
import com.ng.pixeldungeon.Challenges;
import com.ng.pixeldungeon.Dungeon;
import com.ng.pixeldungeon.ResultDescriptions;
import com.ng.pixeldungeon.actors.hero.Hero;
import com.ng.pixeldungeon.actors.hero.HeroClass;
import com.ng.pixeldungeon.items.artifacts.Artifact;
import com.ng.pixeldungeon.items.artifacts.HornOfPlenty;
import com.ng.pixeldungeon.ui.BuffIndicator;
import com.ng.pixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class Hunger extends Buff implements Hero.Doom {

	private static final float STEP	= 10f;

	public static final float HUNGRY	= 260f;
	public static final float STARVING	= 360f;

	private static final String TXT_HUNGRY		= "You are hungry.";
	private static final String TXT_STARVING	= "You are starving!";
	private static final String TXT_DEATH		= "You starved to death...";

	private float level;
	private float partialDamage;

	private static final String LEVEL			= "level";
	private static final String PARTIALDAMAGE 	= "partialDamage";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( LEVEL, level );
		bundle.put( PARTIALDAMAGE, partialDamage );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		level = bundle.getFloat( LEVEL );
		partialDamage = bundle.getFloat(PARTIALDAMAGE);
	}

	@Override
	public boolean act() {

		if (Dungeon.level.locked){
			spend(STEP);
			return true;
		}

		if (target.isAlive()) {

			Hero hero = (Hero)target;

			if (isStarving()) {

				partialDamage += target.HT/100f;

				if (partialDamage > 1){
					target.damage( (int)partialDamage, this);
					partialDamage -= (int)partialDamage;
				}
				
			} else {

				float newLevel = level + STEP;
				boolean statusUpdated = false;
				if (newLevel >= STARVING) {

					GLog.n( TXT_STARVING );
					hero.resting = false;
					hero.damage( 1, this );
					statusUpdated = true;

					hero.interrupt();

				} else if (newLevel >= HUNGRY && level < HUNGRY) {

					GLog.w( TXT_HUNGRY );
					statusUpdated = true;

				}
				level = newLevel;

				if (statusUpdated) {
					BuffIndicator.refreshHero();
				}

			}

			float step = ((Hero)target).heroClass == HeroClass.ROGUE ? STEP * 1.2f : STEP;
			spend( target.buff( Shadows.class ) == null ? step : step * 1.5f );

		} else {

			diactivate();

		}

		return true;
	}

	public void satisfy( float energy ) {

		Artifact.ArtifactBuff buff = target.buff( HornOfPlenty.hornRecharge.class );
		if (buff != null && buff.isCursed()){
			energy *= 0.67f;
			GLog.n("The cursed horn steals some of the food energy as you eat.");
		}

		if (!Dungeon.isChallenged(Challenges.NO_FOOD))
			reduceHunger( energy );
	}

	//directly interacts with hunger, no checks.
	public void reduceHunger( float energy ) {

		level -= energy;
		if (level < 0) {
			level = 0;
		} else if (level > STARVING) {
			level = STARVING;
		}

		BuffIndicator.refreshHero();
	}

	public boolean isStarving() {
		return level >= STARVING;
	}

	@Override
	public int icon() {
		if (level < HUNGRY) {
			return BuffIndicator.NONE;
		} else if (level < STARVING) {
			return BuffIndicator.HUNGER;
		} else {
			return BuffIndicator.STARVATION;
		}
	}

	@Override
	public String toString() {
		if (level < STARVING) {
			return "Hungry";
		} else {
			return "Starving";
		}
	}

	@Override
	public String desc() {
		String result;
		if (level < STARVING) {
			result = "You can feel your stomach calling out for food, but it's not too urgent yet.\n\n";
		} else {
			result = "You're so hungry it hurts.\n\n";
		}

		result += "Hunger slowly increases as you spend time in the dungeon, eventually you will begin to starve. " +
				"While starving you will slowly lose health instead of regenerating it.\n" +
				"\n" +
				"Rationing is important! If you have health to spare starving isn't a bad idea if it means there will " +
				"be more food later. Effective rationing can make food last a lot longer!\n\n";

		return result;
	}

	@Override
	public void onDeath() {

		Badges.validateDeathFromHunger();

		Dungeon.fail( ResultDescriptions.HUNGER );
		GLog.n( TXT_DEATH );
	}
}
