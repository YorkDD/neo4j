/*
 * Copyright (c) 2002-2017 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.internal.compiler.v3_2.pipes

import org.neo4j.cypher.internal.compiler.v3_2._
import org.neo4j.cypher.internal.compiler.v3_2.commands._
import org.neo4j.cypher.internal.compiler.v3_2.commands.values.KeyToken
import org.neo4j.cypher.internal.compiler.v3_2.planDescription.Id
import org.neo4j.cypher.internal.compiler.v3_2.IndexDescriptor

class ConstraintOperationPipe(op: PropertyConstraintOperation, keyToken: KeyToken, propertyKey: KeyToken)
                             (val id: Id = new Id)(implicit val monitor: PipeMonitor) extends Pipe {
  protected def internalCreateResults(state: QueryState): Iterator[ExecutionContext] = {
    val keyTokenId = keyToken.getOrCreateId(state.query)
    val propertyKeyId = propertyKey.getOrCreateId(state.query)

    op match {
      case _: CreateUniqueConstraint => state.query.createUniqueConstraint(IndexDescriptor(keyTokenId, propertyKeyId))
      case _: DropUniqueConstraint   => state.query.dropUniqueConstraint(IndexDescriptor(keyTokenId, propertyKeyId))
      case _: CreateNodePropertyExistenceConstraint => state.query.createNodePropertyExistenceConstraint(keyTokenId, propertyKeyId)
      case _: DropNodePropertyExistenceConstraint => state.query.dropNodePropertyExistenceConstraint(keyTokenId, propertyKeyId)
      case _: CreateRelationshipPropertyExistenceConstraint => state.query.createRelationshipPropertyExistenceConstraint(keyTokenId, propertyKeyId)
      case _: DropRelationshipPropertyExistenceConstraint => state.query.dropRelationshipPropertyExistenceConstraint(keyTokenId, propertyKeyId)
    }

    Iterator.empty
  }
}
