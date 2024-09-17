package org.itaya.pixivservice

import org.itaya.pixivservice.model.ArtworkInfo
import org.itaya.pixivservice.service.ArtworkDownloadService
import org.itaya.pixivservice.service.ArtworkSelectService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.nio.file.Path

@SpringBootTest
class PixivserviceApplicationTests @Autowired constructor(
    val artworkSelectService: ArtworkSelectService,
    val artworkDownloadService: ArtworkDownloadService
) {
    @Test
    fun example() {
        val idList = ArrayList<ArtworkInfo>();
        artworkSelectService.selectionBaseOnArtworkID {
            idList.addAll(selectRecommended(50))
        }
        artworkDownloadService.downloadArtworkAsFile(idList) {
            startAt(Path.of("download"))
            nextLevel(true)
            addFolder {
                it.folderName = "SFW"
                it.filterConfig.visibility = ArtworkInfo.Visibility.SFW
            }
            addFolder {
                it.folderName = "NSFW"
                it.filterConfig.visibility = ArtworkInfo.Visibility.NSFW
            }
            nextLevel(true)
            addFolder {
                it.folderName = "viewsGreaterThan3000"
                it.filterConfig.viewsGreaterThan = 3000
            }
            addFolder {
                it.folderName = "viewsSmallerThan3000"
                it.filterConfig.viewsSmallerThan = 3000
            }
        }
    }
}
