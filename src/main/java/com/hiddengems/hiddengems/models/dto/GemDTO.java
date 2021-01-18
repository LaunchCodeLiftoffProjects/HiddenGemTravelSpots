package com.hiddengems.hiddengems.models.dto;

import com.hiddengems.hiddengems.models.Gem;
import com.hiddengems.hiddengems.models.GemCategory;

import javax.validation.constraints.NotNull;

public class GemDTO {

    @NotNull
    private Gem gem;

    @NotNull
    private GemCategory gemCategory;

    public GemDTO() {}

    public GemDTO(Gem gem, GemCategory gemCategory) {
        this.gem = gem;
        this.gemCategory = gemCategory;
    }

    public Gem getGem() { return gem; }

    public void setGem(Gem gem) { this.gem = gem; }

    public GemCategory getGemCategory() { return gemCategory; }

    public void setGemCategory(GemCategory gemCategory) { this.gemCategory = gemCategory; }

}
