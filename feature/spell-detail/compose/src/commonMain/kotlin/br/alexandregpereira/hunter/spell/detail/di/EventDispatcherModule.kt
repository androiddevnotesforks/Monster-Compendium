/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.spell.detail.di

import br.alexandregpereira.hunter.spell.detail.SpellDetailAnalytics
import br.alexandregpereira.hunter.spell.detail.SpellDetailEventManager
import br.alexandregpereira.hunter.spell.detail.SpellDetailViewModel
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventDispatcher
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventListener
import org.koin.dsl.module

val featureSpellDetailModule = module {
    single { SpellDetailEventManager() }
    single<SpellDetailEventDispatcher> { get<SpellDetailEventManager>() }
    single<SpellDetailEventListener> { get<SpellDetailEventManager>() }

    single {
        SpellDetailViewModel(
            getSpell = get(),
            spellDetailEventListener = get(),
            dispatcher = get(),
            analytics = SpellDetailAnalytics(get()),
            appLocalization = get(),
        )
    }
}
