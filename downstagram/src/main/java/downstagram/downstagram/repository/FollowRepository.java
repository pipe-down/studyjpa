package downstagram.downstagram.repository;

import downstagram.downstagram.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    long countByFollowerId(Long id);
    long countByFollowingId(Long id);

    long countByFollowerIdAndFollowingUserId(Long id, String userId); // 팔로우 되어있는지 count하는 메서드

    @Modifying
    @Transactional
    void deleteByFollowingIdAndFollowerId(Long id1, Long id2); // 언팔로우 메서드

}
