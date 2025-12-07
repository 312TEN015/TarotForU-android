package com.fourleafclover.tarot.demo.data

import com.fourleafclover.tarot.data.CardResultData
import com.fourleafclover.tarot.data.OverallResultData
import com.fourleafclover.tarot.data.TarotOutputDto

val demoTarotResult = TarotOutputDto(
    tarotId = "0",
    tarotType = 0,
    cards = arrayListOf(1, 2, 3),
    createdAt_ = "2024-01-14T12:38:23.000Z",
    cardResults = arrayListOf(
        CardResultData(
            keywords = arrayListOf("애정", "친밀감", "새로운 분야", "인기"),
            description = "사람들 사이의 정에 무척 민감해지고 사람들과 애정이 깊어지는 때"
        ),
        CardResultData(
            keywords = arrayListOf("갈림길", "이득보다 의리", "함께", "정체"),
            description = "신의를 지키며 먼저 다가가세요"
        ),
        CardResultData(
            keywords = arrayListOf("경쟁", "적극적", "현명한 지인", "지성"),
            description = "평소보다 외향적인 성향이 강해지게 되므로 사람들 앞에 적극적으로 나서는 일이 많아집니다"
        )
    ),
    overallResult = OverallResultData(
        summary = "오늘 당신은 존재만으로도 상대에게 큰 위로가 될 수 있습니다",
        full = "오늘 당신과 상대는 서로 교감이 잘 될 수 있는 하루입니다. 그래서 남들에게는 할 수 없었던 고민도 다 들어 주면서 위로와 함께 적절한 조언도 곁들여 줄 수 있습니다. 지친 마음을 어루만져 줄 수 있는 당신은 존재만으로도 위로가 될 수 있습니다. 당신과 상대를 치유하는 성향은 서로의 휴식처가 될 것입니다. 이에 따라 안정적인 연애를 할 수 있고, 상대에게 당신이 필요할 것입니다. 서로를 의지하게 되면서 관계가 발전될 수 있습니다. 당신이 솔로이거나 호감을 느끼고 있다면, 당신의 이런 성향을 잘 어필하면 좋습니다. 서로가 가까워지기까지는 조금 시간이 필요할 수 있지만, 한번 애정을 주고 믿음을 주기 시작하면 그 마음이 참 오래가는 일편단심인 사랑을 할 수 있는 기운입니다. 그동안 과거의 연애나 현재 연애가 힘들었다면, 서로의 상처를 보듬어주면 정신적으로 만족감이 커질 수 있습니다. 오늘 당신의 존재만으로 진정한 위안이 되어줄 수 있는 하루인 만큼 상대에게 따뜻한 말 한마디를 건네시길 바랍니다."
    )
)