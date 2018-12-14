package com.galvanize.badgearamaregistration.repository;

import com.galvanize.badgearamaregistration.entity.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitorRepository extends CrudRepository<Person, Long> {
void deleteByPhoneNumber(Long phone);
Optional<Person> findByPhoneNumber(Long phone);
Boolean existsByPhoneNumber(Long phone);
}
