import CardView from '../Components/CardView';
import { StyleSheet, Text, View, Image, ScrollView, Dimensions, TouchableOpacity, Modal, Pressable, SafeAreaView} from 'react-native';
import { useEffect, useState } from 'react';
import LoadingPage from './LoadingPage';
import { db } from '../firebaseConfig';

// import StackNavigator from '../navigation/StackNavigator';
import { useNavigation } from '@react-navigation/native';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import LikeModal from './LikeModal';

const mainImg = 'https://storage.googleapis.com/sparta-image.appspot.com/lecture/main.png';
const category = ['전체','일상생활', '반려견', '재테크'];

export default function MainPage() {
    const [dataSet, isDataSet] = useState(false)
    const [data, setData] = useState([]);
    const [cate, setCate] = useState('전체')

    /* 모달창을 위한 설정 */
    const navigation = useNavigation();
    const [modalVisible, setModalVisible] = useState(false);

    const handleModalVisible = () => {
      setModalVisible(!modalVisible)
    }

    /***** useEffect() *****/
    useEffect(()=>{
        //LikeButton 아이콘 나타내기
        navigation.setOptions({
          headerRight: () => (
            <TouchableOpacity onPress={()=> setModalVisible(true)}>
                <MaterialIcons style={{ marginRight: 10 }} name="favorite" size='24' color='white' />
            </TouchableOpacity>
        )})

        db.ref('/tip').on('value', (snapshot) => {
          if(snapshot.exists()) {
            setData(snapshot.val())
            isDataSet(true);
            console.log('reDataSet')
          } else {
            console.log('no tips!');
            return null;
          }
        });
    },[])
    
    return(
      isDataSet?
      <View style={styles.safeAreaContainer}>
        <ScrollView style={styles.scrollViewContainer}>
            <View style={styles.containerOne}>
              <Image source={{uri:mainImg}}
                    style={{width: Dimensions.get('screen').width-16, height:200, borderRadius:18}}></Image>
            </View>
      
            <View style={styles.containerTwo}>
              <ScrollView horizontal={true} showsHorizontalScrollIndicator={false}>
                {
                  category.map((value, i) => {
                    return(
                      <TouchableOpacity style={(cate==value)?styles.scrollSelectedBtn:styles.scrollNoSelectedBtn} key={i}>
                        <Text style={(cate==value)?styles.scrollSelectedBtnText:styles.scrollNoSelectedBtnText} onPress={()=> (setCate(value))}>{value}</Text>
                      </TouchableOpacity>
                    )
                  })
                }
              </ScrollView>       
      
              <View style={styles.postBoard}>
                { (cate=='전체') ?
                  data.map((value, i)=> {
                    return(
                      <CardView value={value} key={i}/>
                    )
                  })
                : data.filter((content => content.category == cate )).map((value, i)=> {
                  return(
                    <CardView value={value} key={i}/>
                  )
                  }) 
                }
              </View>
            </View>

          </ScrollView>
          <LikeModal modalVisible={modalVisible} handleModalVisible={handleModalVisible}/>
        </View>
    : <LoadingPage/>
    )

}

const styles = StyleSheet.create({
    safeAreaContainer: {
      flex: 1,
    },
    scrollViewContainer: {
      backgroundColor: '#fff'
    },
    category: {
      fontWeight: 500,
      paddingVertical: 5,
      fontSize: 20,
    },
    containerOne: {
      flex: 1,
      alignItems: 'flex-start',
      padding: 8
    },
    containerTwo: {
      flex: 4,
      flexDirection: 'column',
      paddingHorizontal: 5,
    }, 
    horizonalScroll: {
      flex: 1,
      flexDirection: 'row',
    },
    scrollSelectedBtn: {
      flex:1,
      flexDirection: 'column',
      justifyContent: 'center',
      width: 100,
      height: 50,
      borderRadius: 12,
      borderWidth: 1,
      borderStyle: 'solid',
      borderColor: '#00a86b',
      backgroundColor:'#00a86b',
      marginHorizontal: 2,
    },
    scrollSelectedBtnText: {
      color: '#fff',
      textAlign: 'center',
      fontWeight: 500
    },
    scrollNoSelectedBtn: {
      flex:1,
      flexDirection: 'column',
      justifyContent: 'center',
      width: 100,
      height: 50,
      borderRadius: 12,
      borderWidth: 1,
      borderStyle: 'solid',
      borderColor: '#69c9a4',
      backgroundColor:'#fff',
      marginHorizontal: 2,
    },
    scrollNoSelectedBtnText: {
      color: '#00a86b',
      textAlign: 'center',
      fontWeight: 500
    },
    postBoard: {
      flex: 5,
      flexDirection: 'column',
      paddingHorizontal: 2,
      paddingTop: 5
    },
  });