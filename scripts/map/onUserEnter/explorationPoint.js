const hidden = Array();

function start() {
    ms.npcDisableInfo(hidden);
    if (ms.getMapId() === 104000000) {
        ms.showEffect("maplemap/enter/104000000", 0);
    }
}