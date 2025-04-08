import { Share } from "react-native";
import { SafeAreaView, StyleSheet, Image, View, Text, TouchableOpacity} from "react-native";
import { ScrollView } from "react-native-gesture-handler";
import { db } from "../firebaseConfig"
import { useEffect, useRef, useState } from "react";
import { set } from "firebase/database";

export default function DetailPage( {route} ) {
    const tip = route.params.detailTip
    const [keep, setKeep] = useState(true);
    const key = useRef(null)      //tip의 저장 인덱스

    /*
    1. like db 있는지 없는지
    1-1 없으면, 생성해서 인덱스 0에 추가
    1-2 있으면, 해당 데이터 인덱스 찾아서 있으면 안넣고 없으면 넣음.
    */

    const addLike = () => {
        // keep이 가능한 상태라면
        if(keep) {
            db.ref('like/').push(tip, () => {
                // console.log(index.current, ' added!')
            }).then(()=> setKeep(false))
        }
    }

    const removeLike = () => {
        if(!keep) {
            db.ref('like/'+key.current).remove().then(()=> {
                setKeep(true)
            })
        }
    }
   
    const onShare = async() => {
        Share.share({
            message:
                tip.title + '\n\n' + tip.desc + '\n\n'
        })
    }

    const checkLikeStatus = async () => {
        const snapshot = await db.ref('/like').once('value');

        if(snapshot.exists()) {        //likeDB 있는 경우
            let found=false;
            snapshot.forEach((v)=> {
                if(v.val().idx==tip.idx) {
                    key.current=v.key
                    found=true;
                    setKeep(false)
                }
            })
        }
    }

    useEffect(()=> {
        checkLikeStatus()
    },[])

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
                            { (keep) ?
                            <TouchableOpacity style={styles.likeButton} onPress={() => addLike()}>
                                <Text style={{textAlign: 'center', color: '#fff'}}>팁 찜하기</Text>
                            </TouchableOpacity>
                            :
                            <TouchableOpacity style={styles.likeRemoveButton} onPress={() => removeLike()}>
                                <Text style={{textAlign: 'center', color: '#fff'}}>찜 해제하기</Text>
                            </TouchableOpacity>
                            }
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
    },
    likeRemoveButton: {
        marginRight: 1,
        borderRadius: 15,
        backgroundColor: '#b22222',
        width: 100,
        height: 50,
        justifyContent: 'center',
        alignSelf: 'center',
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