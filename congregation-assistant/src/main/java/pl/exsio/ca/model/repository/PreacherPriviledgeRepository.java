/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.exsio.ca.model.repository;

import pl.exsio.ca.model.dao.PreacherPriviledgeDao;
import pl.exsio.ca.model.entity.PreacherPriviledgeImpl;
import pl.exsio.frameset.core.repository.GenericJpaRepository;

/**
 *
 * @author exsio
 */
public interface PreacherPriviledgeRepository extends GenericJpaRepository<PreacherPriviledgeImpl, Long>, PreacherPriviledgeDao<PreacherPriviledgeImpl> {
    
}
