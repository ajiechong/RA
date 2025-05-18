package DatabaseTest;

import cn.example.researchassistant.service.FavoriteService;
import org.junit.jupiter.api.BeforeEach;

public class FavoriteServiceTest {

    private FavoriteService favoriteService;

    @BeforeEach
    public void setUp() {
        favoriteService = new FavoriteService();
    }
}