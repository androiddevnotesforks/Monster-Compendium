/*
 * Copyright 2023 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.data.monster.local.entity

import androidx.room.Embedded
import androidx.room.Entity

sealed class DamageEntity {
    abstract val value: ValueEntity
}

@Entity(primaryKeys = ["index", "monsterIndex"])
data class DamageVulnerabilityEntity(
    @Embedded override val value: ValueEntity,
) : DamageEntity()

@Entity(primaryKeys = ["index", "monsterIndex"])
data class DamageResistanceEntity(
    @Embedded override val value: ValueEntity,
) : DamageEntity()

@Entity(primaryKeys = ["index", "monsterIndex"])
data class DamageImmunityEntity(
    @Embedded override val value: ValueEntity,
) : DamageEntity()