package org.itaya.pixivservice

import org.itaya.pixivservice.model.ArtworkInfo
import org.itaya.pixivservice.service.ArtworkDownloadService
import org.itaya.pixivservice.service.ArtworkSelectService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.nio.file.Path

@SpringBootTest
class PixivserviceApplicationTests {
    @Test
    fun example() {
        PixivServiceStartup.setCookie("")
        PixivServiceStartup.startup { selector, filter, downloader ->
            val idList = ArrayList<ArtworkInfo>();
            selector.selectionBaseOnArtworkID {
                idList.addAll(selectRecommended(50))
            }
            downloader.downloadArtworkAsFile(idList) {
                startAt(Path.of("download"))
                nextHierarchy(true)
                addFolder {
                    folderName = "SFW"
                    filterConfig.visibility = ArtworkInfo.Visibility.SFW
                }
                addFolder {
                    folderName = "NSFW"
                    filterConfig.visibility = ArtworkInfo.Visibility.NSFW
                }
                nextHierarchy(true)
                addFolder {
                    folderName = "viewsGreaterThan3000"
                    filterConfig.viewsGreaterThan = 3000
                }
                addFolder {
                    folderName = "viewsSmallerThan3000"
                    filterConfig.viewsSmallerThan = 3000
                }
            }
        }
    }
}
