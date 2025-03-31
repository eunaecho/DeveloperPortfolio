import { Share } from "react-native";
import { SafeAreaView, StyleSheet, Image, View, Text, TouchableOpacity} from "react-native";
import { ScrollView } from "react-native-gesture-handler";
import { db } from "../firebaseConfig"
import { useCallback, useEffect, useState, useRef } from "react";

export default function DetailPage( {route} ) {
    const tip = route.params.detailTip
    // const [tip, setTip] = useState(route.params.detailTip);
    const keep = useRef(true)

    console.log("DB 객체 : ", db)

    /*
    1. like db 있는지 없는지?
    1-1 없으면, 생성해서 인덱스 0에 추가
    1-2 있으면, 해당 데이터 인덱스 찾아서 있으면 안넣고 없으면 넣음.
    */
    const keepData = () => {
        keep.current?
        db.ref('/like').once('value',(snapshot) => {
            keep.current = false
            if(!snapshot.exists()) {
                db.ref('like/0/').set(tip, () => {
                    console.log('like 생성! & tip 저장!')
                })}
            else {
                var keepThis = true;
                // db 데이터 인덱스로 해당 데이터 있는지 확인
                // 있으면 -> 그냥 넘어가
                for(let childSnapshot of snapshot.val()) {
                    if(childSnapshot.idx==tip.idx){
                        keepThis = false;
                        console.log('이미 추가된 찜!!')
                        return
                    }
                }
                //없으면 -> 수 세서 저장
                if(keepThis){
                    db.ref('like/' + snapshot.numChildren() + '/').set(tip, () => {
                        console.log(tip.idx, ' added!')
                })}
            }})
        : console.log('keep 상태 : false!!')
    }
   
    const onShare = async() => {
        Share.share({
            message:
                tip.title + '\n\n' + tip.desc + '\n\n'
        })
    }

    return (
        <>
            <SafeAreaView style={styles.container}>
                    <Image source={{ uri:tip.image }} style={styles.imageContainer} ></Image>
                    <View style={styles.descContainer}>
                        <Text style={styles.titleText}>{tip.title}</Text>
                        <ScrollView style={styles.descScroll}>
                            <Text style={styles.descText}>{tip.desc}</Text>
                        </ScrollView>
                    </View>

                    <View style={styles.btnContainer}>
                        <TouchableOpacity style={styles.likeButton} onPress={() => keepData()}>
                            <Text style={{textAlign: 'center'}}>팁 찜하기</Text>
                        </TouchableOpacity>
                        <TouchableOpacity style={styles.linkButton} onPress={() => onShare()}>
                            <Text style={{textAlign: 'center'}}>공유하기</Text>
                        </TouchableOpacity>
                    </View>
            </SafeAreaView>
        </>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff'
    },
    imageContainer: {
        flex: 3,
        padding:10,
        width: '100%',
        height: '100%',
        borderRadius: 20,
    },
    descContainer: {
        flex:2,
        padding: 10
    },
    descScroll: {
        width: '95%',
        paddingTop: 3,
        alignContent: 'center',
    },
    titleText: {
        fontSize: 20,
        fontWeight: 600,
        textAlign: 'center'
    }, 
    descText: {
        flex: 2,
        marginTop: 5,
        fontSize: 15,
        fontWeight: 200,
        lineHeight: 23,
        textAlign: 'center'
    },
    btnContainer : {
        flex: 0.5,
        flexDirection: 'row',
        justifyContent: 'center',
        alignContent: 'center'
    },
    likeButton: {
        marginRight: 1,
        borderRadius: 15,
        backgroundColor: '#00a86b',
        width: 100,
        height: 50,
        justifyContent: 'center',
        alignSelf: 'center',
        shadowColor: '#00a86b'
    },
    linkButton: {
        marginLeft: 1,
        borderRadius: 15,
        backgroundColor: '#fef233',
        borderColor: '#00a86b',
        width: 100,
        height: 50,
        justifyContent: 'center',
        alignSelf: 'center'
    }
})